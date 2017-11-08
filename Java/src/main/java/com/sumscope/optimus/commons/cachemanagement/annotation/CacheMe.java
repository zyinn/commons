package com.sumscope.optimus.commons.cachemanagement.annotation;

import com.sumscope.optimus.commons.cachemanagement.EmptyCacheResolver;
import com.sumscope.optimus.commons.cachemanagement.SSCacheResolver;

import java.lang.annotation.*;

/**
 * Created by fan.bai on 2016/4/5.
 * 缓存注释。当某非void的方法被注释，表明该方法将被缓存。
 * 使用注释的先决条件是预先注册相应的基于AOP的处理对象。建议使用Spring框架，然后在ApplicationContext.xml中加入：
 *      <bean id="cacheMeAop"
 *           class="com.sumscope.optimus.commons.cachemanagement.CacheMeAnnAOPProcess">
 *      </bean>
 *  由于基于AOP解析，该注释不能用在本地函数上直接调用。必须用在某被Spring管理的类的方法上。请参考Sample中的使用方法
 *  当使用configuration方式开发应用时，需要在Configuration类中申明
 *   @Bean
 *   public CacheMeAnnAOPProcess cacheMeAnnAOPProcess(){
 *        return new CacheMeAnnAOPProcess();
 *   }
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheMe {
    /**
     * @return 过期时间，单位为秒。过期时间过后所有缓存设置为无效。默认10分钟。
     */
    long timeout() default 600;

    /**
     * 缓存更新模式。
     * true：同步更新模式。此时当缓存进行更新时，所有并发的调用程序进入等待状态，直到获取了最新的数据。
     * false：异步更新模式，可能读到脏数据。当缓存进行更新时，调用程序立刻获得缓存数据（脏数据）并触发一个异步的更新程序。
     * 注意，在异步模式下，不应存在返回null的情况。否则
     */
    boolean synchornizeUpdate() default true;

    /**
     * @return 无效权重，意义为当缓存不被命中，保留多少次循环后删除。当设置为-1时表明始终保留不删除。循环时间即为过期时间。
     * 比如过期时间设置为10秒，该参数为5，则表明在累积50秒内该缓存一直不被命中则删除。
     * 累积50秒意思是：当一个缓存先有4*10秒不被命中，接着有10秒被命中记录，然后又有2*10秒不被命中，权重计算为4-1+2 = 5
     */
    int maxUselessWeights() default 5;

    /**
     * @return 实现SSCacheResolver接口的类，该接口用于缓存数据的更新。默认策略为不更新缓存。
     */
    Class<? extends SSCacheResolver> notifyResolver() default EmptyCacheResolver.class;

}
