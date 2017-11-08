package com.sumscope.optimus.commons.messagebus.activemq;


import com.sumscope.optimus.commons.messagebus.AbstractJmsMessageSubscriber;
import com.sumscope.optimus.commons.messagebus.MessageBusInitialException;
import com.sumscope.optimus.commons.messagebus.MessageBusType;
import org.springframework.stereotype.Service;

/**
 * Created by fan.bai on 2016/3/31.
 * Active MQ bus receiver
 */
@Service
public class ActiveMQSubscriber extends AbstractJmsMessageSubscriber {

    public ActiveMQSubscriber()  {
        super(MessageBusType.ACTIVEMQ);
    }

    public ActiveMQSubscriber(String busurl) {
        super(MessageBusType.ACTIVEMQ,busurl);
    }


    @Override
    protected String getSpecificTopic() {
        return getTopic();
    }

}
