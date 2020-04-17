package com.xiaowei.market.bean.db;

import java.math.BigDecimal;

import com.xiaowei.market.bean.entiy.IdEntity;
import lombok.Getter;
import lombok.Setter;
/**
 * 
 * WARNING:
 * this code is generated by code-generator
 * please DO NOT modify this file
 * @author code-generator
 * @date 2020-04-16 16:00:52
 */
@Getter
@Setter
public class Goods extends IdEntity {
  /**
   * 商品名称
   */
  private String goodsName;
  /**
   * 商品标题
   */
  private String goodsTitle;
  /**
   * 商品的图片
   */
  private String goodsImg;
  /**
   * 商品的详情介绍
   */
  private String goodsDetail;
  /**
   * 商品单价
   */
  private BigDecimal goodsPrice;
  /**
   * 商品库存，-1表示没有限制
   */
  private Integer goodsStock;

}