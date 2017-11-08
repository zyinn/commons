package com.sumscope.optimus.commons.exceptions;

import java.util.List;

/**
 * Created by fan.bai on 2016/7/26.
 * 数据验证异常Dto
 */
public class ValidationExceptionDto extends ExceptionDto {
    /**
     * 数据错误的详细说明列表
     */
    private List<ValidationErrorDetailsDto> errorDetailsList;

    public List<ValidationErrorDetailsDto> getErrorDetailsList() {
        return errorDetailsList;
    }

    public void setErrorDetailsList(List<ValidationErrorDetailsDto> errorDetailsList) {
        this.errorDetailsList = errorDetailsList;
    }
}
