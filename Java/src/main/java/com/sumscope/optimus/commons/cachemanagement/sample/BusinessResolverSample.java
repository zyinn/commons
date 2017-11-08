package com.sumscope.optimus.commons.cachemanagement.sample;

import com.sumscope.optimus.commons.cachemanagement.CachedDataContainer;
import com.sumscope.optimus.commons.cachemanagement.SSCacheResolver;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by fan.bai on 2016/4/7.
 * 一个实际Resolver的例子
 */
public class BusinessResolverSample implements SSCacheResolver<String> {

    @Override
    public boolean isCacheable(Object[] params) {
        return true;
    }

    @Override
    public void updateCache(CachedDataContainer container, String convertedChangedData, String source, Object[] params) {
        List<String> data = (List<String>) container.getCachedData();
        data.add(0, convertedChangedData);
        System.out.println(source);
    }

    @Override
    public boolean isUpdateNeeded(String convertedChangedData, Object[] params) {
        return true;
    }

    @Override
    public String convertToCachedType(Object changedData) {
        String newString = (String) changedData;
        return newString;
    }

    @Override
    public Set<Class> acceptedResultTypes() {
        Set<Class> value = new HashSet<>();
        value.add(String.class);
        return value;
    }
}
