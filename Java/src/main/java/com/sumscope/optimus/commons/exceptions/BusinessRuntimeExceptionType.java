package com.sumscope.optimus.commons.exceptions;

/**
 * Created by fan.bai on 2016/5/25.
 * 通用程序异常类型
 */
public enum BusinessRuntimeExceptionType implements ExceptionType{
    /**
     * 前台传递的Dto数据校验失败
     */
    PARAMETER_INVALID("E0001","参数校验错误"),
    /**
     * 认证失败
     */
    AUTHORIZE_INVALID("E8888","认证失败"),
    /**
     * 数据库错误
     */
    DATABASE_ERROR("E1001","数据库错误"),
    /**
     * 总线错误
     */
    MESSAGE_BUS_ERROR("E2001","总线错误"),
    /**
     * WebSocket错误
     */
    WEB_SOCKET_ERROR("E3001","WebSocket错误"),
    /**
     * 其他错误
     */
    OTHER("E9999","其他运行错误");

    BusinessRuntimeExceptionType(String code,String info) {
        this.code = code;
        this.errorInfoCN = info;
    }

    private String code;
    private String errorInfoCN;

    @Override
    public String getExceptionCode() {
        return code;
    }

    @Override
    public String getExceptionInfoCN() {
        return errorInfoCN;
    }
}
