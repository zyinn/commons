package com.sumscope.optimus.commons.exceptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fan.bai on 2016/7/26.
 * 验证异常。服务端对web传递的Dto会进行数据验证。当数据不符合验证条件时，将抛出该验证异常。该异常包含了一个
 * 未通过验证数据的列表，Web端应根据该异常及其包含的异常明细进行处理。可弹出对话框警示或在现有窗口进行违法数据标识
 */
public class ValidationException extends  BusinessRuntimeException{
    /**
     * 验证异常的详细说明，说明是哪些字段的数据有异常
     */
    private List<ValidationExceptionDetails> exceptionDetails = new ArrayList<>();

    public ValidationException(){
        super(BusinessRuntimeExceptionType.PARAMETER_INVALID,"数据验证异常！");
    }

    public ValidationException(List<ValidationExceptionDetails> detailsList){
        super(BusinessRuntimeExceptionType.PARAMETER_INVALID,"数据验证异常！");
        this.exceptionDetails = detailsList;
    }

    public List<ValidationExceptionDetails> getExceptionDetails() {
        return exceptionDetails;
    }

    public void setExceptionDetails(List<ValidationExceptionDetails> exceptionDetails) {
        this.exceptionDetails = exceptionDetails;
    }

    public void addValidationExceptionDetails(ValidationExceptionDetails details){
        this.exceptionDetails.add(details);
    }
}
