package com.sumscope.optimus.commons.cachemanagement;

/**
 * Created by fan.bai on 2016/4/6.
 * 支持定时作废或刷新缓存的服务
 */
public interface SSTimeoutCacheService extends SSCacheService {
    /**
     * 设置Timeout时间
     *
     * @param timeout Timeout时间
     */
    void setTimeout(long timeout);


}
