package com.sumscope.optimus.commons.messagebus;

import javax.jms.Message;

/**
 * Created by fan.bai on 2016/5/9.
 * 监听器抽象类，根据总线类型调用相关函数
 */
public abstract class AbstractBusinessMessageListener implements BusinessMessageListener {
    @Override
    public void onMessageReceived(String topic, MessageBusType busType, Object sourceMessage) {
        switch (busType) {
            case ACTIVEMQ:
                processJMSMessage(topic, (Message) sourceMessage);
                break;
            case QPID:
                processJMSMessage(topic, (Message) sourceMessage);
                break;
        }

    }


    protected abstract void processJMSMessage(String topic, Message sourceMessage);
}
