package com.xiaowei.market.mapper;

/**
 * @author wang wei
 * 2020/4/16 16:06
 */
public interface BaseMapper<T> {
    int add(T var1);

    int delete(Long var1);

    int updateIgnoreNull(T var1);

    int update(T var1);

    T get(Long var1);
}