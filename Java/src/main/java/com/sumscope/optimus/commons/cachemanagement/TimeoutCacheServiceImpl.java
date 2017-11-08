package com.sumscope.optimus.commons.cachemanagement;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by fan.bai on 2016/4/6.
 * 支持定时过期的缓存服务
 */
public class TimeoutCacheServiceImpl extends CacheServiceImpl implements SSTimeoutCacheService {
    private long timeoutTime;
    private int maxUselessWeights;

    @Override
    public void setTimeout(long timeout) {
        this.timeoutTime = timeout;
        startTimer();
    }

    private void startTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimeTask(), timeoutTime * 1000, timeoutTime * 1000);
    }

    //每次Timeout的定时循环都将hittimes清零并标记缓存为脏
    private void markDirtyAndRemoveUseless() {
        //使用ConcurrentHashMap可以保证线程安全
        for (Object[] params : cache.keySet()) {
            CachedDataContainer cachedDataContainer = cache.get(params);
            cachedDataContainer.setDirty(true);
            calculateInvalidWeightAndRemoveUseless(params, cachedDataContainer);
            cachedDataContainer.clearHitTimes();
        }
    }

    private void calculateInvalidWeightAndRemoveUseless(Object[] params, CachedDataContainer cachedDataContainer) {
        if (cachedDataContainer instanceof CachedDataTimeoutContainer) {
            // 缓存命中将递增命中次数。每一缓存的InvalidWeight 属性表明不被使用的权值。如果命中次数在一次Timeout循环中
            // 是0这权值加1，否则递减，最多递减至0。 如果该值超出MAX_INVALID_WEIGHT ，表明该缓存被命中
            // 的次数太少，不再值得继续保留，需要从缓存中移除
            if (cachedDataContainer.getHitTimes() <= 0) {
                ((CachedDataTimeoutContainer) cachedDataContainer).accumulateInvalidWeight();
            } else {
                ((CachedDataTimeoutContainer) cachedDataContainer).reduceInvalidWeight();
            }
            if (maxUselessWeights > 0 &&
                    ((CachedDataTimeoutContainer) cachedDataContainer).getInvalidWeight() > maxUselessWeights) {
                cache.remove(params);
            }
        }
    }

    @Override
    protected CachedDataContainer createCachedDataContainer() {
        return new CachedDataTimeoutContainer();
    }

    public void setMaxUselessWeights(int maxUselessWeights) {
        this.maxUselessWeights = maxUselessWeights;
    }

    class TimeTask extends TimerTask {
        @Override
        public void run() {
            markDirtyAndRemoveUseless();
        }
    }
}
