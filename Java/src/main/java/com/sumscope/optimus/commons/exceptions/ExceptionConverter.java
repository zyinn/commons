package com.sumscope.optimus.commons.exceptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fan.bai on 2016/6/17.
 * 将RuntimeException 转换为Dto
 */
public class ExceptionConverter {
    private static final String NEW_LINE_STRING = "\r\n";

    public static ExceptionDto convertException(Exception e) {
        if (e == null) {
            return null;
        }
        ExceptionDto result = new ExceptionDto();
        if (e instanceof BusinessRuntimeException) {
            if (e instanceof ValidationException) {
                //数据验证异常的处理
                result = getValidationExceptionDto((ValidationException) e);
            }
            BusinessRuntimeException be = (BusinessRuntimeException) e;
            ExceptionType exceptionType = be.getExceptionType();
            if (exceptionType != null) {
                result.setExceptionCode(exceptionType.getExceptionCode());
                result.setExceptionName(exceptionType.getExceptionInfoCN());
            }
            setInfoToDto(e, result);
        } else {
            result.setExceptionCode(BusinessRuntimeExceptionType.OTHER.getExceptionCode());
            result.setExceptionName(BusinessRuntimeExceptionType.OTHER.getExceptionInfoCN());
            setInfoToDto(e, result);

        }
        return result;
    }

    private static ExceptionDto getValidationExceptionDto(ValidationException e) {
        ValidationExceptionDto result = new ValidationExceptionDto();
        List<ValidationErrorDetailsDto> list = new ArrayList<>();
        if (e.getExceptionDetails() != null) {
            for (ValidationExceptionDetails details : e.getExceptionDetails()) {
                list.add(convertToValidationErrorDetailsDto(details));
            }
        }
        return result;
    }

    private static ValidationErrorDetailsDto convertToValidationErrorDetailsDto(ValidationExceptionDetails details) {
        ValidationErrorDetailsDto result = null;
        if (details != null) {
            result = new ValidationErrorDetailsDto();
            result.setDetailMsg(details.getDetailMsg());
            result.setSourceFiled(details.getSourceField());
            result.setValidationErrorType(details.getValidationErrorType().getErrorTypeString());
        }
        return result;
    }

    private static void setInfoToDto(Exception e, ExceptionDto result) {
        result.setExceptionMessage(e.getMessage());
        StringBuilder errorInfo = new StringBuilder();
        StackTraceElement[] stackTrace = e.getStackTrace();
        if (stackTrace != null) {
            for (StackTraceElement ele : stackTrace) {
                errorInfo.append(ele.toString()).append(NEW_LINE_STRING);
            }
            result.setExceptionStackTrace(errorInfo.toString());
        }
    }
}
