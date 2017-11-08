package com.sumscope.optimus.commons.cachemanagement;

import com.sumscope.optimus.commons.cachemanagement.annotation.CacheMe;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Created by fan.bai on 2016/4/5.
 * 对CacheMe注释的解析处理
 */
@Aspect
public class CacheMeAnnAOPProcess {
    @Around("@annotation(cacheMeAnn)")
    public Object aroundAdvice(ProceedingJoinPoint proceedingJoinPoint, CacheMe cacheMeAnn) throws Throwable {
        boolean validCacheProcess = false;
        Signature signature = proceedingJoinPoint.getSignature();
        if (signature instanceof MethodSignature) {
//        只缓存有返回值的且类型为"方法调用"的方法，如果将@CacheMe使用在非方法调用,或者void方法上，本解析不处理。
            MethodSignature methodSignature = (MethodSignature) signature;
            Class returnType = methodSignature.getReturnType();
            if (!"void".equals(returnType.getName())) {
                validCacheProcess = true;
            }
        }
        if (validCacheProcess) {
            String className = proceedingJoinPoint.getTarget().getClass().getName();
            String methodName = signature.getName();
            String key = className + "." + methodName;
            SSTimeoutCacheService timeoutCacheService = SSCacheManagementFactory.
                    getTimeoutCacheService(key, cacheMeAnn.timeout(), cacheMeAnn.notifyResolver(),cacheMeAnn.maxUselessWeights());
            return timeoutCacheService.getCachedResult(cacheMeAnn.synchornizeUpdate(),proceedingJoinPoint);
        } else {
            return proceedingJoinPoint.proceed();
        }


    }


}
