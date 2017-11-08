package com.sumscope.optimus.commons.cachemanagement;

import com.sumscope.optimus.commons.cachemanagement.annotation.NotifyMyParameter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Created by fan.bai on 2016/4/5.
 * 对NotifyMyParameter注释的解析处理
 */
@Aspect
public class NotifyMyParameterAnnAOPProcess {
    @Around("@annotation(notifyMyParamAnn)")
    public Object aroundAdvice(ProceedingJoinPoint proceedingJoinPoint, NotifyMyParameter notifyMyParamAnn) throws Throwable {
        boolean invalidProcess = false;
        Signature signature = proceedingJoinPoint.getSignature();
        String methodName = signature.getName();
        String parameterName = notifyMyParamAnn.parameter();
        Object parameter = null;
//        仅在表示的参数名合法情况下进行缓存数据更新处理
        if (parameterName == null || "".equals(parameterName)) {
            invalidProcess = true;
        }
//        注释中的参数名必须符合方法中的参数名，否则不处理
        if (!invalidProcess && signature instanceof MethodSignature) {
            MethodSignature methodSignature = (MethodSignature) signature;
            String[] parameterNames = methodSignature.getParameterNames();
            int index = indexInArray(parameterName, parameterNames);
            if (index < 0) {
                invalidProcess = true;
            } else {
                Object[] args = proceedingJoinPoint.getArgs();
                parameter = args[index];
            }
        } else {
            invalidProcess = true;
        }
//      对缓存的更新将在独立线程中进行，这样不影响当前方法的执行
        if (!invalidProcess) {
            String className = proceedingJoinPoint.getTarget().getClass().getName();
            String source = className + "." + methodName + ":" + parameterName;
            SSCacheManagementFactory.notifyUpdateResult(parameter, source);
        }

        return proceedingJoinPoint.proceed();


    }

    private int indexInArray(String parameterName, String[] parameterNames) {
        int value = -1;
        for (int i = 0; i < parameterNames.length; i++) {
            if (parameterNames[i].equals(parameterName)) {
                value = i;
                break;
            }
        }
        return value;
    }


}
