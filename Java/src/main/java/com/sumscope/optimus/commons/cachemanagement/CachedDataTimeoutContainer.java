package com.sumscope.optimus.commons.cachemanagement;

/**
 * Created by fan.bai on 2016/4/7.
 * 支持过期时间的缓存容器类。
 */
public class CachedDataTimeoutContainer extends CachedDataContainer {
    //用于判断该缓存是否值得继续保留。若一次过期周期内没有命中过一次该缓存，则该值递增
    private int invalidWeight;

    void accumulateInvalidWeight() {
        invalidWeight++;
    }

    void reduceInvalidWeight() {
        if (invalidWeight > 0) {
            invalidWeight--;

        }
    }

    public int getInvalidWeight() {
        return invalidWeight;
    }
}
