package com.sumscope.optimus.commons.messagebus;

/**
 * Created by fan.bai on 2016/3/31.
 * Listen to a message bus
 */
public interface MessageBusSubscriber extends MessageBusSubPub {

    void setSubscriberListener(BusinessMessageListener listener);

    void startToListen() throws MessageBusException, MessageBusInitialException;
}
