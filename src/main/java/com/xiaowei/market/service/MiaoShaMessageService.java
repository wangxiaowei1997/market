package com.xiaowei.market.service;

import com.xiaowei.market.domain.MiaoShaMessageInfo;
import com.xiaowei.market.domain.MiaoShaMessageUser;
import com.xiaowei.market.bean.vo.MiaoShaMessageVo;
import com.xiaowei.market.mapper.MiaoshaMessageMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class MiaoShaMessageService {

    @Resource
    private MiaoshaMessageMapper miaoshaMessageMapper;

    public List<MiaoShaMessageInfo> getmessageUserList( Long userId , Integer status ){
        return miaoshaMessageMapper.listMiaoShaMessageByUserId(userId,status);
    }


    @Transactional(rollbackFor = Exception.class)
    public void insertMs(MiaoShaMessageVo miaoShaMessageVo){
        MiaoShaMessageUser mu = new MiaoShaMessageUser() ;
        mu.setUserId(miaoShaMessageVo.getUserId());
        mu.setMessageId(miaoShaMessageVo.getMessageId());
        miaoshaMessageMapper.insertMiaoShaMessageUser(mu);
        MiaoShaMessageInfo miaoshaMessage = new MiaoShaMessageInfo();
        miaoshaMessage.setContent(miaoShaMessageVo.getContent());
//        miaoshaMessage.setCreateTime(new Date());
        miaoshaMessage.setStatus(miaoShaMessageVo.getStatus());
        miaoshaMessage.setMessageType(miaoShaMessageVo.getMessageType());
        miaoshaMessage.setSendType(miaoShaMessageVo.getSendType());
        miaoshaMessage.setMessageId(miaoShaMessageVo.getMessageId());
        miaoshaMessage.setCreateTime(new Date());
        miaoshaMessage.setMessageHead(miaoShaMessageVo.getMessageHead());
        miaoshaMessageMapper.insertMiaoShaMessage(miaoshaMessage);
    }
}
