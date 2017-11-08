package com.sumscope.optimus.commons.exceptions;

/**
 * Created by fan.bai on 2016/7/26.
 * 数据异常的类型
 */
public interface ValidationErrorType {
    /**
     * @return 类型的String表达，该表达将被前端解析
     */
    String getErrorTypeString();
}
