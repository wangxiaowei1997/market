package com.xiaowei.market.service;

import com.xiaowei.market.bean.db.MiaoshaUser;
import com.xiaowei.market.exception.GlobleException;
import com.xiaowei.market.mapper.MiaoshaUserMapper;
import com.xiaowei.market.rabbitmq.MQSender;
import com.xiaowei.market.redis.MiaoShaUserKey;
import com.xiaowei.market.redis.RedisService;
import com.xiaowei.market.utils.MD5Utils;
import com.xiaowei.market.utils.UUIDUtil;
import com.xiaowei.market.bean.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

import static com.xiaowei.market.common.enums.ResultStatus.*;

@Service
public class MiaoShaUserService {

    public static final String COOKIE_NAME_TOKEN = "token" ;
    private static Logger logger = LoggerFactory.getLogger(MiaoShaUserService.class);

    @Resource
    private MiaoshaUserMapper miaoshaUserMapper;
    @Resource
    private RedisService redisService ;
    @Resource
    private MQSender sender ;


    public MiaoshaUser getByToken(HttpServletResponse response , String token) {

        if(StringUtils.isEmpty(token)){
            return null ;
        }
        MiaoshaUser user =redisService.get(MiaoShaUserKey.token,token,MiaoshaUser.class) ;
        if(user!=null) {
            addCookie(response, token, user);
        }
        return user ;

    }

    public MiaoshaUser getByNickName(String nickName) {
        //取缓存
        MiaoshaUser user = redisService.get(MiaoShaUserKey.getByNickName, ""+nickName, MiaoshaUser.class);
        if(user != null) {
            return user;
        }
        //取数据库
        user = miaoshaUserMapper.getByNickname(nickName);
        if(user != null) {
            redisService.set(MiaoShaUserKey.getByNickName, ""+nickName, user);
        }
        return user;
    }


    // http://blog.csdn.net/tTU1EvLDeLFq5btqiK/article/details/78693323
    public boolean updatePassword(String token, String nickName, String formPass) {
        //取user
        MiaoshaUser user = getByNickName(nickName);
        if(user == null) {
            throw new GlobleException(MOBILE_NOT_EXIST);
        }
        //更新数据库
        MiaoshaUser toBeUpdate = new MiaoshaUser();
        toBeUpdate.setNickname(nickName);
        toBeUpdate.setPassword(MD5Utils.formPassToDBPass(formPass, user.getSalt()));
        miaoshaUserMapper.update(toBeUpdate);
        //处理缓存
        redisService.delete(MiaoShaUserKey.getByNickName, ""+nickName);
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(MiaoShaUserKey.token, token, user);
        return true;
    }


    public boolean register(HttpServletResponse response , String userName , String passWord , String salt) {
        MiaoshaUser miaoShaUser =  new MiaoshaUser();
        miaoShaUser.setNickname(userName);
        String DBPassWord =  MD5Utils.formPassToDBPass(passWord , salt);
        miaoShaUser.setPassword(DBPassWord);
        miaoShaUser.setRegisterDate(new Date());
        miaoShaUser.setSalt(salt);
        miaoShaUser.setNickname(userName);
        try {
            miaoshaUserMapper.insertMiaoShaUser(miaoShaUser);
            MiaoshaUser user = miaoshaUserMapper.getByNickname(miaoShaUser.getNickname());
            if(user == null){
                return false;
            }
            //生成cookie 将session返回游览器 分布式session
            String token= UUIDUtil.uuid();
            addCookie(response, token, user);
        } catch (Exception e) {
            logger.error("注册失败",e);
            return false;
        }
        return true;
    }

    public boolean login(HttpServletResponse response , LoginVo loginVo) {
        if(loginVo ==null){
            throw  new GlobleException(SYSTEM_ERROR);
        }

        String mobile =loginVo.getMobile();
        String password =loginVo.getPassword();
        MiaoshaUser user = getByNickName(mobile);
        if(user == null) {
            throw new GlobleException(MOBILE_NOT_EXIST);
        }

        String dbPass = user.getPassword();
        String saltDb = user.getSalt();
        String calcPass = MD5Utils.formPassToDBPass(password,saltDb);
        if(!calcPass.equals(dbPass)){
            throw new GlobleException(PASSWORD_ERROR);
        }
        //生成cookie 将session返回游览器 分布式session
        String token= UUIDUtil.uuid();
        addCookie(response, token, user);
        return true ;
    }




    public String createToken(HttpServletResponse response , LoginVo loginVo) {
        if(loginVo ==null){
            throw  new GlobleException(SYSTEM_ERROR);
        }

        String mobile =loginVo.getMobile();
        String password =loginVo.getPassword();
        MiaoshaUser user = getByNickName(mobile);
        if(user == null) {
            throw new GlobleException(MOBILE_NOT_EXIST);
        }

        String dbPass = user.getPassword();
        String saltDb = user.getSalt();
        String calcPass = MD5Utils.formPassToDBPass(password,saltDb);
        if(!calcPass.equals(dbPass)){
            throw new GlobleException(PASSWORD_ERROR);
        }
        //生成cookie 将session返回游览器 分布式session
        String token= UUIDUtil.uuid();
        addCookie(response, token, user);
        return token ;
    }
    private void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {
        redisService.set(MiaoShaUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        //设置有效期
        cookie.setMaxAge(MiaoShaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
