package com.sumscope.optimus.commons.messagebus;

/**
 * Created by fan.bai on 2016/3/31.
 * 标准接口，用于数据发送。基于publish-subscribe模式
 */
public interface MessageBusPublisher extends MessageBusSubPub {
    void publishBusinessMessage(byte[] msg) throws MessageBusException, MessageBusInitialException;
    void close() throws MessageBusException;
}
