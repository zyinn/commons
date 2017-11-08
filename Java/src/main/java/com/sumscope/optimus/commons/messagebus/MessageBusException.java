package com.sumscope.optimus.commons.messagebus;

import java.io.IOException;

/**
 * Created by fan.bai on 2016/4/1.
 * 封装Messagebus异常
 */
public class MessageBusException extends Exception {
    public MessageBusException(String msg, Throwable e){
        super(msg,e);
    }

    public MessageBusException(Throwable e){
        super(e);
    }
    public MessageBusException(IOException e){
        super(e);
    }
    public MessageBusException(ClassNotFoundException e){
        super(e);
    }
}
