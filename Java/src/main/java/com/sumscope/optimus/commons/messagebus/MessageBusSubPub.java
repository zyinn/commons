package com.sumscope.optimus.commons.messagebus;

/**
 * Created by fan.bai on 2016/3/31.
 * 标准接口，用于数据发送接收。基于publish-subscribe模式
 */
public interface MessageBusSubPub {
    MessageBusType getMessageBusType();

    String getTopic();

    String getBusAddress();

}
