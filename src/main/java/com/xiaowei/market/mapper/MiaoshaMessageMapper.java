package com.xiaowei.market.mapper;

import com.xiaowei.market.bean.db.MiaoshaMessage;
import com.xiaowei.market.domain.MiaoShaMessageInfo;
import com.xiaowei.market.domain.MiaoShaMessageUser;
import org.apache.ibatis.annotations.Param;
import java.util.List;


public interface MiaoshaMessageMapper extends BaseMapper<MiaoshaMessage>{

     List<MiaoShaMessageInfo> listMiaoShaMessage(@Param("messageId") String messageId);

     List<MiaoShaMessageUser> listMiaoShaMessageUser(@Param("messageId") String messageId);

     void insertMiaoShaMessage(MiaoShaMessageInfo miaoShaMessage);

     void insertMiaoShaMessageUser(MiaoShaMessageUser miaoShaMessageUser);

     List<MiaoShaMessageInfo> listMiaoShaMessageByUserId(@Param("userId") long userId, @Param("status") Integer status);
}