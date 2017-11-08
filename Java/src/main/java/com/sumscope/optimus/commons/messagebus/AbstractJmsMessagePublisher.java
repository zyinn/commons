package com.sumscope.optimus.commons.messagebus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

/**
 * Created by fan.bai on 2016/4/1.
 * 提供基于JMS的数据发布服务
 */
public abstract class AbstractJmsMessagePublisher extends AbstractMessageBusSubPub implements MessageBusPublisher {
    Logger logger = LoggerFactory.getLogger(AbstractJmsMessagePublisher.class);
    private MessageProducer producer;

    public AbstractJmsMessagePublisher(MessageBusType type)  {
        super(type);
    }

    public AbstractJmsMessagePublisher(MessageBusType type,String url)  {
        super(type);
        setBusAddress(url);
    }


    @Override
    public void publishBusinessMessage(byte[] msg) throws MessageBusException, MessageBusInitialException {
        try {
            JMSManager.getInstance().initialJMS(getBusAddress(), getMessageBusType());
            Session session = JMSManager.getInstance().getSession(getBusAddress());
            BytesMessage bytesMessage = session.createBytesMessage();
            bytesMessage.writeBytes(msg);
            getJmsProducer(session).send(bytesMessage);
        } catch (JMSException e) {
            logger.error("Failed to publish message!", e);
            throw new MessageBusException(e);
        } catch (MessageBusInitialException e) {
            logger.error("Failed to publish message!", e);
            throw e;
        }
    }

    private MessageProducer getJmsProducer(Session session) throws JMSException {
        if (producer == null) {
            Topic topic = session.createTopic(getSpecificTopic());
            producer = session.createProducer(topic);

        }
        return producer;
    }

    protected abstract String getSpecificTopic();


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (producer != null) {
            producer.close();
        }
    }

    @Override
    public void close() throws MessageBusException {
        try {
            producer.close();
            producer = null;
        } catch (JMSException e) {
            logger.error("Failed to close producer!", e);
            e.printStackTrace();
            throw new MessageBusException(e);
        }
    }
}
