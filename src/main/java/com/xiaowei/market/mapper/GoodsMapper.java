package com.xiaowei.market.mapper;

import com.xiaowei.market.bean.db.Goods;
import com.xiaowei.market.bean.vo.GoodsVo;
import com.xiaowei.market.domain.MiaoshaGoods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsMapper extends BaseMapper<Goods>{
    /**
     * 查询商品列表
     */
    List<GoodsVo> listGoodsVo();

    /**
     * 根据商品id查询商品信息
     */
    GoodsVo getGoodsVoByGoodsId(@Param("goodsId") long goodsId);

    /**
     * 库存扣减
     */
    int reduceStock(MiaoshaGoods g);
}