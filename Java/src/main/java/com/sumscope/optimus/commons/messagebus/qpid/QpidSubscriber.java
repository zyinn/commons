package com.sumscope.optimus.commons.messagebus.qpid;

import com.sumscope.optimus.commons.messagebus.AbstractJmsMessageSubscriber;
import com.sumscope.optimus.commons.messagebus.MessageBusInitialException;
import com.sumscope.optimus.commons.messagebus.MessageBusType;
import org.springframework.stereotype.Service;

/**
 * Created by fan.bai on 2016/3/31.
 * Receiver for Qpid Message Bus
 */
@Service
public class QpidSubscriber extends AbstractJmsMessageSubscriber {

    public QpidSubscriber()  {
        super(MessageBusType.QPID);
    }

    public QpidSubscriber(String busurl)  {
        super(MessageBusType.QPID,busurl);
    }

    @Override
    protected String getSpecificTopic() {
        return QpidTopicHelper.getQpidTopic(getTopic());

    }

}
