package com.xiaowei.market.utils;


import org.dozer.DozerBeanMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author wang wei
 * 2020/4/16 17:35
 */
public class BeanCopyUtil {
    private static DozerBeanMapper dozer = new DozerBeanMapper();

    public BeanCopyUtil() {
    }

    public static <T> T map(Object source, Class<T> destinationClass) {
        return dozer.map(source, destinationClass);
    }

    public static <T> List<T> mapList(Collection sourceList, Class<T> destinationClass) {
        List<T> destinationList = new ArrayList();
        Iterator var3 = sourceList.iterator();

        while(var3.hasNext()) {
            Object sourceObject = var3.next();
            T destinationObject = dozer.map(sourceObject, destinationClass);
            destinationList.add(destinationObject);
        }

        return destinationList;
    }

    public static void copy(Object source, Object destinationObject) {
        dozer.map(source, destinationObject);
    }
}
