package com.sumscope.optimus.commons.exceptions;

/**
 * Created by fan.bai on 2016/7/26.
 * 数据异常的详细说明Dto
 */
public class ValidationErrorDetailsDto {
    /**
     * 数据验证对应Dto的字段，使用“.”分隔子对象的属性。可能的属性为"name","contacts.name"
     */
    private String sourceFiled;
    /**
     * 错误的类型
     */
    private String validationErrorType;
    /**
     * 详细的错误说明
     */
    private String detailMsg;

    public String getSourceFiled() {
        return sourceFiled;
    }

    public void setSourceFiled(String sourceFiled) {
        this.sourceFiled = sourceFiled;
    }

    public String getValidationErrorType() {
        return validationErrorType;
    }

    public void setValidationErrorType(String validationErrorType) {
        this.validationErrorType = validationErrorType;
    }

    public String getDetailMsg() {
        return detailMsg;
    }

    public void setDetailMsg(String detailMsg) {
        this.detailMsg = detailMsg;
    }
}
