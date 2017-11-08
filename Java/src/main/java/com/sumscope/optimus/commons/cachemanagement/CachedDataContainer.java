package com.sumscope.optimus.commons.cachemanagement;

/**
 * Created by fan.bai on 2016/4/6.
 * 缓存容器。记录实际的缓存数据并提供几个属性用于缓存管理
 */
public class CachedDataContainer {

    /**
     * 用于线程同步控制。大部分的线程同步在各自的缓冲容器上。这样不同参数的调用不会被同步以提高效率。
     */
    protected final LockObject lock;
    /**
     * true表明该容器保留的数据已无效，需要从新调用实际方法获取数据
     */
    private boolean dirty;

    /**
     * 实际的缓存数据。与使用注释的方法返回类型一致
     */
    private Object cachedData;
    /**
     * true表明某线程正在进行缓存数据的更新，此时应进入同步模块避免数据错误读取
     */
    private boolean inProcessing;
    /**
     * 用于记录命中次数。该属性可被用于缓存清理的判断
     */
    private long hitTimes;

    /**
     * 初始化标记。表明容器生成后是否通过实际方法调用获取到数据。用于异步更新模式下，第一次方法调用的并发场景
     */
    private boolean initialized = false;

    public CachedDataContainer() {
        lock = new LockObject();
    }

    public long getHitTimes() {
        return hitTimes;
    }

    public void clearHitTimes() {
        this.hitTimes = 0;
    }

    public void accumulateHitTimes() {
        this.hitTimes++;
    }


    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public Object getCachedData() {
        return cachedData;
    }

    public void setCachedData(Object cachedData) {
        this.cachedData = cachedData;
    }

    public boolean isInProcessing() {
        return inProcessing;
    }

    public void setInProcessing(boolean inProcessing) {
        this.inProcessing = inProcessing;
    }


    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }
}
