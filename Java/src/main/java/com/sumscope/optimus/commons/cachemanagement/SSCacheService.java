package com.sumscope.optimus.commons.cachemanagement;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Created by fan.bai on 2016/4/6.
 * 执行缓存服务，该服务提供缓冲的执行，定时刷新及更新数据刷新缓冲功能
 */
public interface SSCacheService {
    /**
     * 根据参数获取缓存中的数据，若无命中或该参数不缓存，则调用实际方法获取返回值
     * @param invocation 根据AspectJ传来的方法调用对象，若无命中则调用方法获取数据
     * @return 命中的数据或调用返回的数据
     */
    Object getCachedResult(boolean synchronizeUpdate, final ProceedingJoinPoint invocation) throws Throwable;

    /**
     * @param resolverClass 业务用户开发的SSCacheResolver对象，用于数据的更新及缓存判断
     */
    void setResolver(Class<? extends SSCacheResolver> resolverClass);

    /**
     * 当数据有更新时，调用该方法，根据业务定义的方法更新缓存的数据，或忽略不更新。
     *
     * @param result 一般是从总线监听得到的数据
     * @param source 数据来源，应该是一个方法的全名加该数据对应的参数名，中间使用冒号分隔。
     *               比如: com.sumscope.optimus.service.SampleService.receiveNewData:data
     */
    void notifyUpdatedResults(Object result, String source);
}
