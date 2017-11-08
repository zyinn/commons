package com.sumscope.optimus.commons.cachemanagement;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fan.bai on 2016/4/6.
 * 缓存管理工厂对象。一个进程使用一个对象用于缓存管理
 */
public class SSCacheManagementFactory {
    private final static Map<String, SSCacheService> cacheServices = new HashMap<>();

    protected static SSTimeoutCacheService getTimeoutCacheService(String key, long timeout, Class<? extends SSCacheResolver> SSCacheResolverClass, int maxUselessWeights) {
        SSTimeoutCacheService ssCacheService = (SSTimeoutCacheService) cacheServices.get(key);
        if (ssCacheService == null) {
//            需进行同步，否则并发时可能出现多个线程生成多个服务对象，则各服务对象间无法进行线程并发控制。
            synchronized (cacheServices) {
//                进入同步块后再次判断是否已经有其他线程实现了服务对象
                ssCacheService = (SSTimeoutCacheService) cacheServices.get(key);
                if (ssCacheService == null) {
                    TimeoutCacheServiceImpl timeoutCacheService = new TimeoutCacheServiceImpl();
                    timeoutCacheService.setResolver(SSCacheResolverClass);
                    timeoutCacheService.setTimeout(timeout);
                    timeoutCacheService.setMaxUselessWeights(maxUselessWeights);
                    cacheServices.put(key, timeoutCacheService);
                    ssCacheService = timeoutCacheService;
                }
            }
        }
        return ssCacheService;

    }

    public static synchronized void notifyUpdateResult(Object result) {
        notifyUpdateResult(result, SSCacheResolver.UNKNOWN_SOURCE);
    }

    public static synchronized void notifyUpdateResult(Object result, String source) {
//        对缓存的更新将在独立线程中进行
        Runnable run = () -> {
            for (String key : cacheServices.keySet()) {
                SSCacheService ssCacheService = cacheServices.get(key);
                ssCacheService.notifyUpdatedResults(result,source);
            }
        };
        Thread thread = new Thread(run);
        thread.start();
    }
}
