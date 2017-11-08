package com.sumscope.optimus.commons.messagebus;

/**
 * Created by fan.bai on 2016/3/31.
 * 消息总线消息发布接收模式基类。基于Topic的publish-subscribe模式。
 */
public abstract class AbstractMessageBusSubPub implements MessageBusSubPub {
    protected String busAddress;
    protected String topic;
    private final MessageBusType messageBusType;

    public AbstractMessageBusSubPub(MessageBusType type) {
        this.messageBusType = type;
    }

    @Override
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getBusAddress() {
        return busAddress;
    }

    public void setBusAddress(String busAddress) {
        this.busAddress = busAddress;
    }

    public MessageBusType getMessageBusType() {
        return messageBusType;
    }



}
