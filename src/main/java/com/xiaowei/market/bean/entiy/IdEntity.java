package com.xiaowei.market.bean.entiy;

/**
 * @author wang wei
 * 2020/4/16 16:04
 */
public abstract class IdEntity extends PKEntity<Long> {
    private Long id;

    public IdEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
