package com.sumscope.optimus.commons.cachemanagement;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by fan.bai on 2016/4/6.
 * SSCacheResolver的一个空实现。当无需对新增结果进行缓存刷新时，使用该实现
 */
public final class EmptyCacheResolver implements SSCacheResolver {
    @Override
    public boolean isCacheable(Object[] params) {
        return true;
    }

    @Override
    public void updateCache(CachedDataContainer container, Object convertedChangedData, String source, Object[] params) {

    }

    @Override
    public boolean isUpdateNeeded(Object convertedChangedData, Object[] params) {
        return false;
    }

    @Override
    public Object convertToCachedType(Object changedData) {
        return changedData;
    }

    @Override
    public Set<Class> acceptedResultTypes() {
        return new HashSet<>();
    }
}
