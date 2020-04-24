package com.xiaowei.market.bean.vo;

import com.xiaowei.market.bean.db.OrderInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailVo {
	private GoodsVo goods;
	private OrderInfo order;
}
