package com.xiaowei.market.mapper;

import com.xiaowei.market.bean.db.MiaoshaUser;
import org.apache.ibatis.annotations.*;

public interface MiaoshaUserMapper extends BaseMapper<MiaoshaUser>{

     /**
      * 根据昵称查询
      */
     MiaoshaUser getByNickname(@Param("nickname") String nickname) ;
     /**
      * 根据id查询
      */
     MiaoshaUser getById(@Param("id") long id) ;
     /**
      * 插入秒杀用户
      */
     void insertMiaoShaUser(MiaoshaUser miaoshaUser);
}