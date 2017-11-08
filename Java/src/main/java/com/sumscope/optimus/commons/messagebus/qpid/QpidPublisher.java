package com.sumscope.optimus.commons.messagebus.qpid;

import com.sumscope.optimus.commons.messagebus.AbstractJmsMessagePublisher;
import com.sumscope.optimus.commons.messagebus.MessageBusInitialException;
import com.sumscope.optimus.commons.messagebus.MessageBusType;
import org.springframework.stereotype.Service;

/**
 * Created by fan.bai on 2016/4/1.
 * 用于Qpid总线的数据发布类
 */
@Service
public class QpidPublisher extends AbstractJmsMessagePublisher {

    public QpidPublisher()  {
        super(MessageBusType.QPID);
    }

    public QpidPublisher(String url)  {
        super(MessageBusType.QPID,url);
    }

    @Override
    protected String getSpecificTopic() {
        return QpidTopicHelper.getQpidTopic(getTopic());
    }
}
