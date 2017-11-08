package com.sumscope.optimus.commons.messagebus;

/**
 * Created by fan.bai on 2016/4/1.
 * 初始化消息总线产生的异常
 */
public class MessageBusInitialException extends Exception {
    public MessageBusInitialException(String msg, Throwable e){
        super(msg,e);
    }

    public MessageBusInitialException(Throwable e){
        super(e);
    }
}
