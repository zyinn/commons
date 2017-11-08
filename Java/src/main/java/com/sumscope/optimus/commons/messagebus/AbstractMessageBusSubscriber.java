package com.sumscope.optimus.commons.messagebus;

/**
 * Created by fan.bai on 2016/3/31.
 * 消息subscriber基类
 */
public abstract class AbstractMessageBusSubscriber extends AbstractMessageBusSubPub implements MessageBusSubscriber {

    private BusinessMessageListener messageBusListener;

    public AbstractMessageBusSubscriber(MessageBusType type) {
        super(type);
    }




    @Override
    public void setSubscriberListener(BusinessMessageListener messageBusListener) {
        this.messageBusListener = messageBusListener;
    }



    protected void fireBusinessListener(Object sourceMessage) {
        if (messageBusListener != null) {
            messageBusListener.onMessageReceived(getTopic(),getMessageBusType(),sourceMessage);
        }
    }

}
