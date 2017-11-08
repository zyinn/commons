package com.sumscope.optimus.commons.exceptions;

/**
 * 异常包装类
 *
 * 在包装时装入原始异常对象, 以便于需要输出调用堆栈
 *
 * Created by qikai.yu on 2016/4/26.
 */
public class BusinessRuntimeException extends RuntimeException {
    private ExceptionType exceptionType;

    /**
     * 使用 特定 异常类型, 包装原始异常对象
     */
    public BusinessRuntimeException(ExceptionType exceptionType, Exception e) {
        super(e);
        this.exceptionType = exceptionType;
    }

        /**
     * 使用特定异常类型, 和消息字符串包装出异常对象
     * @param exceptionType
     * @param message
     */
    public BusinessRuntimeException(ExceptionType exceptionType, String message) {
        super(message);
        this.exceptionType = exceptionType;
    }

    @Override
    public String toString() {
        return "BusinessRuntimeException [exceptionType=" + exceptionType.getExceptionCode() + "]";
    }

    public ExceptionType getExceptionType() {
        return exceptionType;
    }
}
