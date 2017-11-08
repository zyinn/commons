package com.sumscope.optimus.commons.exceptions;

/**
 * Created by fan.bai on 2016/7/26.
 * 数据验证错误明细，表明是Web端传递数据的哪一个字段错误以及什么类型的错误
 */
public class ValidationExceptionDetails {
    /**
     * 数据验证对应Dto的字段，使用“.”分隔子对象的属性。可能的属性为"name","contacts.name"
     */
    private String sourceField;
    /**
     * 错误的类型
     */
    private ValidationErrorType validationErrorType;
    /**
     * 错误的详细说明
     */
    private String detailMsg;

    /**
     * @param errorType ValidationErrorType 的一个类型
     * @param sourceF 引起数据验证异常的字段名称。用“.”分隔子对象。比如： “name”，“contacts.id”
     * @param msg
     */
    public ValidationExceptionDetails(ValidationErrorType errorType,String sourceF, String msg){
        this.validationErrorType = errorType;
        this.sourceField = sourceF;
        this.detailMsg = msg;
    }

    public String getSourceField() {
        return sourceField;
    }

    public ValidationErrorType getValidationErrorType() {
        return validationErrorType;
    }

    public String getDetailMsg() {
        return detailMsg;
    }
}
