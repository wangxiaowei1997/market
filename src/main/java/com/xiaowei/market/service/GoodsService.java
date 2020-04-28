package com.xiaowei.market.service;

import com.xiaowei.market.domain.MiaoshaGoods;
import com.xiaowei.market.bean.vo.GoodsVo;
import com.xiaowei.market.mapper.GoodsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class GoodsService {
	
	@Resource
	private GoodsMapper goodsMapper;


	public List<GoodsVo> listGoodsVo(){
		if(goodsMapper ==null){
			log.info("goodsMapper 是空的");
		}else {
			log.info("goodsMapper 不是空的");

		}
		return goodsMapper.listGoodsVo();
	}

	public GoodsVo getGoodsVoByGoodsId(long goodsId) {
		return goodsMapper.getGoodsVoByGoodsId(goodsId);
	}

	public boolean reduceStock(GoodsVo goods) {
		MiaoshaGoods g = new MiaoshaGoods();
		g.setGoodsId(goods.getId());
		int ret = goodsMapper.reduceStock(g);
		return ret > 0;
	}


	public boolean addStock(GoodsVo goods) {
		MiaoshaGoods g = new MiaoshaGoods();
		g.setGoodsId(goods.getId());
		int ret = goodsMapper.addStock(g);
		return ret > 0;
	}
	
	
	
}
