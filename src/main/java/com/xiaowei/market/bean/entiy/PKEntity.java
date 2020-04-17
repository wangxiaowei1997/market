package com.xiaowei.market.bean.entiy;

import java.io.Serializable;

/**
 * @author wang wei
 * 2020/4/16 16:04
 */
public abstract class PKEntity<PK extends Serializable> extends BaseEntity {
    public PKEntity() {
    }

    public abstract PK getId();

    public abstract void setId(PK var1);
}
