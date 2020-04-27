package com.xiaowei.market.access;

import com.xiaowei.market.redis.key.BasePrefix;

public class AccessKey extends BasePrefix {

	private AccessKey( int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	
	public static AccessKey withExpire(int expireSeconds) {
		return new AccessKey(expireSeconds, "access");
	}
	
}
