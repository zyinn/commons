package com.sumscope.optimus.commons.cachemanagement;

import java.util.Set;

/**
 * Created by fan.bai on 2016/4/6.
 * 缓存更新策略接口。业务系统需要根据各自场景进行定义。一个现成的空对象EmptyCacheResolver已经建立。
 * 注意：实现该接口的类必须提供一个不带参数的构造方法！在实际处理中缓存框架将使用不带参数的构造方法实例化该类。
 */
public interface SSCacheResolver<T> {
    String UNKNOWN_SOURCE = "unknown_source";

    /**
     * 对于某些方法我们只缓存特定的参数组合，该方法用于实现该需求。
     * @param params 方法调用参数，一个数组，如果无参数则数组长度为0。数组顺序即为参数声明的顺序
     * @return true：需要对该参数进行缓存，false：不缓存
     */
    boolean isCacheable(Object[] params);

    /**
     * 对缓存进行更新。需要强制转化container.getData()的数据到实际类型。参数列表可用于判断如何进行更新。
     *
     * @param container            缓存容器
     * @param convertedChangedData 转换后的需更新数据
     * @param source               数据来源，应该是一个方法的全名加该数据对应的参数名，中间使用冒号分隔。若调用者
     *                             未给出则等于UNKNOWN_SOURCE
     * @param params               参数列表
     */
    void updateCache(CachedDataContainer container, T convertedChangedData, String source, Object[] params);

    /**
     * 根据转换后的数据和参数列表进行判断，是否需要对缓存进行更新
     *
     * @param convertedChangedData 转换后的需更新数据
     * @param params               参数列表
     * @return true 需要进行更新，updateCache()函数会被接着调用。
     */
    boolean isUpdateNeeded(T convertedChangedData, Object[] params);

    /**
     * 转换更新数据至合适的数据类型用于后续方法调用
     *
     * @param changedData 需更新的数据
     * @return 转换后的数据
     */
    T convertToCachedType(Object changedData);

    /**
     * 是否更新缓存的第一步判断。若更新的数据属于列表，则继续进行缓存更新。反之则不触发进一步判断。
     * 需要注意的是，实际缓存以及引发缓存更新的数据类型应足够特别以支持有效判断。
     * 比如需更新类型是String，则该类型太过普通很难让实际的商业逻辑进行判断，是否需要根据该数据进行缓存的刷新。
     * 一般应为一个Dto类型
     *
     * @return 所有可能引起缓存更新的数据类型列表
     */
    Set<Class> acceptedResultTypes();
}
