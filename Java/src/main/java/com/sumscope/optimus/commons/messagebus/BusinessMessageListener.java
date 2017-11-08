package com.sumscope.optimus.commons.messagebus;

/**
 * Created by fan.bai on 2016/3/31.
 * Message bus listener
 */
public interface BusinessMessageListener {
    /**
     * Triggered when message is received
     * @param topic topic
     * @param busType Message bus type
     *@param sourceMessage the message from specific message bus
     */
    void onMessageReceived(String topic, MessageBusType busType, Object sourceMessage);
}
