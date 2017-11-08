package com.sumscope.optimus.commons.messagebus.activemq;

import com.sumscope.optimus.commons.messagebus.AbstractJmsMessagePublisher;
import com.sumscope.optimus.commons.messagebus.MessageBusInitialException;
import com.sumscope.optimus.commons.messagebus.MessageBusType;
import org.springframework.stereotype.Service;

/**
 * Created by fan.bai on 2016/4/1.
 * ActiveMQ数据发布类
 */
@Service
public class ActiveMQPublisher extends AbstractJmsMessagePublisher {

    public ActiveMQPublisher()  {
        super(MessageBusType.ACTIVEMQ);
    }

    public ActiveMQPublisher(String url)  {
        super(MessageBusType.ACTIVEMQ,url);
    }

    @Override
    protected String getSpecificTopic() {
        return getTopic();
    }
}
