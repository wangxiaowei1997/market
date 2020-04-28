package com.xiaowei.market.mapper;

import com.xiaowei.market.bean.db.MiaoshaOrder;
import org.apache.ibatis.annotations.Param;

public interface MiaoshaOrderMapper extends BaseMapper<MiaoshaOrder>{

    /**
     * 根据userId和goodsId查询秒杀订单
     */
    MiaoshaOrder getMiaoShaOrderByUserIdAndGoodsId(@Param("userId") Long userId,
                                                   @Param("goodsId") Long goodsId);
}