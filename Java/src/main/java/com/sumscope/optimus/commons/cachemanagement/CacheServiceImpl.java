package com.sumscope.optimus.commons.cachemanagement;

import com.sumscope.optimus.commons.log.LogManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by fan.bai on 2016/4/6.
 * 缓存管理服务。该服务要求线程安全，因此有大量的同步程序块。
 */
abstract class CacheServiceImpl implements SSCacheService {
    private static Logger logger = LoggerFactory.getLogger(CacheServiceImpl.class);
    protected final Map<Object[], CachedDataContainer> cache = new ConcurrentHashMap<>();
    protected SSCacheResolver ssCacheResolver;

    final private LockObject lock = new LockObject();

//    对于异步更新使用一个更新线程处理实际的方法调用
    private Thread updateThread = null;

    @Override
    public Object getCachedResult(boolean synchronizeUpdate,ProceedingJoinPoint invocation) throws Throwable {
        Object[] methodParams = invocation.getArgs();
        Object[] cachedParams = getCachedParam(methodParams);
        Object result;
        if (cachedParams != null) {
//            命中缓存，需要判断是否dirty
            result = getDataFromCache(synchronizeUpdate,invocation, cachedParams);
        } else {
//            缓存未命中，根据业务定义，可能需要初始化
            result = initializeCache(invocation, methodParams);

        }
        return result;
    }

    private Object initializeCache(ProceedingJoinPoint invocation, Object[] methodParams) throws Throwable {
        Object result;//            尚未缓存
        boolean cacheable = true;
        if (ssCacheResolver != null) {
            cacheable = ssCacheResolver.isCacheable(methodParams);
        }
        if (cacheable) {
//                数据需要缓存
            CachedDataContainer newContainer;
//          使用同步Map保证cache的同步
            Object[] cachedParamsInS = getCachedParam(methodParams);
            if (cachedParamsInS == null) {
                newContainer = createCachedDataContainer();
                newContainer.setDirty(true);
                newContainer.setInProcessing(false);
                cache.put(methodParams, newContainer);
            } else {
                newContainer = cache.get(cachedParamsInS);
            }
//            在生成好新的CachedDataContainer对象后，在各自的lock对象上进行同步。
            synchronized (newContainer.lock) {
                result = getDataConsiderDirtyFlagInSynchronizedBlock(invocation, newContainer);
            }
        } else {
//            数据无需缓存，直接调用程序方法获取返回值
            result = invocation.proceed();
        }
        return result;
    }

    //生成一个新的CachedDataContainer实例，子类复写该方法
    protected CachedDataContainer createCachedDataContainer() {
        return new CachedDataContainer();
    }

    private Object getDataFromCache(boolean synchronizeUpdate,ProceedingJoinPoint invocation, Object[] cachedParams) throws Throwable {
        Object result;
        CachedDataContainer cachedDataContainer = cache.get(cachedParams);
//      cachedDataContainer.isInProcessing()提供了一个机制，用于检查是否需要进行线程同步保护，在大多数情况下，也就是缓
//      存数据并未被处理的情况下，可以不用同步保护以提高程序速度。但是需要特别注意，此机制在程序执行顺序角度不能完全保证
//      数据的线程安全。在特定情况下，有可能出现线程1进入读取缓存过程（判断时inProcess = false），而线程二刚刚开始处理缓存
//      （线程1判断语句之后设置 inProcess = true）。然而由于读取大大快于处理，所以不会出现读取到正在处理的缓存数据情况。
        if (!cachedDataContainer.isInProcessing()) {
//            没有正在处理中，若数据有效则直接命中，无需任何同步。若数据无效，进入同步模块，根据数据状态进行方法调用
            if (cachedDataContainer.isDirty()) {
                if(synchronizeUpdate){
                    //同步模式下进入同步模块，进入等待状态
                    synchronized (cachedDataContainer.lock) {
                        result = getDataConsiderDirtyFlagInSynchronizedBlock(invocation, cachedDataContainer);
                    }
                }else{
                    //异步模式下直接获取缓存数据（脏读），并开启新的线程更新缓存
                    result = cachedDataContainer.getCachedData();
                    startAsynUpdateThread(invocation, cachedDataContainer);
                }
            } else {
                result = cachedDataContainer.getCachedData();
                cacheHitted(cachedDataContainer);
            }
        } else {
            //正在处理Cache数据。
            if(synchronizeUpdate){
//            有其他线程正在处理缓存数据，进入同步模块，根据数据状态进行方法调用
                synchronized (cachedDataContainer.lock) {
                    result = getDataConsiderDirtyFlagInSynchronizedBlock(invocation, cachedDataContainer);
                }
            }else{
                //在异步模式下直接读取缓存，此时为脏读
                result = cachedDataContainer.getCachedData();
//                若当前缓存容器尚未初始化，则说明有并发的程序第一次触发方法并尚未完成，需要进入
                //同步模块等待
                if(!cachedDataContainer.isInitialized() ){
                    synchronized (cachedDataContainer.lock) {
                        result = getDataConsiderDirtyFlagInSynchronizedBlock(invocation, cachedDataContainer);
                    }
                }
            }
        }
        return result;
    }

    private void startAsynUpdateThread(final ProceedingJoinPoint invocation, final CachedDataContainer cachedDataContainer) {
        boolean startThread = false;
        //更新线程应该只有一个
        if(updateThread == null){
            synchronized (this.lock) {
                //使用同步模块限制更新线程数量
                if(updateThread == null){
                    updateThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                getDataConsiderDirtyFlagInSynchronizedBlock(invocation, cachedDataContainer);
                                updateThread = null;
                            } catch (Throwable throwable) {
                                LogManager.error("缓存服务调用实例方法错误： " + throwable.toString());
                            }
                        }
                    });
                    startThread = true;
                }
            }
        }
        if(updateThread != null && startThread){
            updateThread.start();
        }
    }

    // 根据数据是否有效进行缓存读取。若数据无效这调用实际方法进行数据读取并放入缓存。若有效则直接命中
//    此方法必须被同步程序块保护，否则可能产生并发错误。
    private Object getDataConsiderDirtyFlagInSynchronizedBlock(ProceedingJoinPoint invocation, CachedDataContainer cachedDataContainer) throws Throwable {
        Object result;
        if (cachedDataContainer.isDirty()) {
            result = resolveResultAndPutToCacheInSynchronizedBlock(cachedDataContainer, invocation);
        } else {
            result = cachedDataContainer.getCachedData();
            cacheHitted(cachedDataContainer);
        }
        return result;
    }

    // 缓存命中，由子类决定如何进行进一步处理。比如累计命中次数
    // 注意，该方法不是线程安全方法。但是一般情况下不会造成问题
    protected void cacheHitted(CachedDataContainer cachedDataContainer) {
        cachedDataContainer.accumulateHitTimes();
    }

    private Object[] getCachedParam(Object[] methodParams) {
        for (Object[] cachedParam : cache.keySet()) {
            if (methodParams.length == cachedParam.length) {
                if (methodParams.length == 0) {
//                    调用无参数
                    return cachedParam;
                } else {
//                    逐一比较参数
                    boolean allEqual = true;
                    for (int i = 0; i < methodParams.length; i++) {
                        Object methodP = methodParams[i];
                        Object cachedP = cachedParam[i];
                        if (!Objects.deepEquals(methodP, cachedP)) {
                            allEqual = false;
                            break;
                        }
                    }
                    if (allEqual) {
                        return cachedParam;
                    }

                }
            }
        }
        return null;
    }

    //  本方法调用实际类的方法获取数据，会比较耗时。本方法必须在同步块中执行
    protected Object resolveResultAndPutToCacheInSynchronizedBlock(CachedDataContainer cachedDataContainer, ProceedingJoinPoint invocation) throws Throwable {
        cachedDataContainer.setInProcessing(true);
        cachedDataContainer.setDirty(true);
        Object value = invocation.proceed();
        cachedDataContainer.setCachedData(value);
        cachedDataContainer.setDirty(false);
        cachedDataContainer.setInProcessing(false);
        cachedDataContainer.setInitialized(true);
        return cachedDataContainer.getCachedData();

    }

    @Override
    public void setResolver(Class<? extends SSCacheResolver> resolverClass) {
        if (resolverClass != EmptyCacheResolver.class) {
            try {
                ssCacheResolver = resolverClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                logger.error("Invalid SSCacheResolver implementation", e);
            }
        }

    }

    @Override
    public void notifyUpdatedResults(Object result, String source) {
        if (ssCacheResolver != null) {
            Class resultClass = getaResultDataJavaClass(result);
            Set acceptedResultTypes = ssCacheResolver.acceptedResultTypes();
            if (CollectionUtils.containsInstance(acceptedResultTypes, resultClass)) {
                Object convertedResult = ssCacheResolver.convertToCachedType(result);
                updateEachCachedData(convertedResult, source);
            }
        }

    }

    private void updateEachCachedData(Object convertedResult, String source) {
        if (source == null || "".equals(source)) {
            source = SSCacheResolver.UNKNOWN_SOURCE;
        }
        for (Object[] params : cache.keySet()) {
            CachedDataContainer cachedDataContainer = cache.get(params);
            if (ssCacheResolver.isUpdateNeeded(convertedResult, params)) {
                synchronized (cachedDataContainer.lock) {
                    ssCacheResolver.updateCache(cachedDataContainer, convertedResult, source, params);
                }
            }
        }
    }

    private Class getaResultDataJavaClass(Object result) {
        if (result instanceof Collection) {
            Collection collection = (Collection) result;
            Iterator iterator = collection.iterator();
            if (iterator.hasNext()) {
                Object element = iterator.next();
                return element.getClass();
            }
        } else {
            return result.getClass();
        }
        return null;
    }
}
