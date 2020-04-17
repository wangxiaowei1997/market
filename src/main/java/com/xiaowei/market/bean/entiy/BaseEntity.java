package com.xiaowei.market.bean.entiy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.Serializable;

/**
 * @author wang wei
 * 2020/4/16 16:03
 */
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 6705418339557986802L;

    public BaseEntity() {
    }

    public String toString() {
        try {
            return this.getClass().getSimpleName() + " = " + JSON.toJSONString(this, new SerializerFeature[]{SerializerFeature.WriteMapNullValue});
        } catch (Exception var2) {
            return super.toString();
        }
    }
}

