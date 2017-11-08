package com.sumscope.optimus.commons.exceptions;

/**
 * Created by fan.bai on 2016/7/26.
 * 数据异常的类型枚举，各项目也可以定义项目专用的数据异常类型。本枚举提供了一般情况下的数据异常说明
 */
public enum GeneralValidationErrorType implements ValidationErrorType {
    /**
     * 非空字段保存了空值
     */
    DATA_MISSING("DATA_MISSING"),
    /**
     * 数据不合法
     */
    DATA_INVALID("DATA_INVALID");

    private String errorTypeString;

    GeneralValidationErrorType(String msg){
        this.errorTypeString = msg;
    }


    @Override
    public String getErrorTypeString() {
        return this.errorTypeString;
    }
}
