package com.sumscope.optimus.commons.messagebus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

/**
 * Created by fan.bai on 2016/3/31.
 * Active MQ and Qpid message bus both use JMS
 */
public abstract class AbstractJmsMessageSubscriber extends AbstractMessageBusSubscriber {
    Logger logger = LoggerFactory.getLogger(AbstractJmsMessageSubscriber.class);
    private MessageConsumer consumer;

    public AbstractJmsMessageSubscriber(MessageBusType type, String busurl) {
        super(type);
        setBusAddress(busurl);
    }

    public AbstractJmsMessageSubscriber(MessageBusType type)  {
        super(type);
    }

    @Override
    public void startToListen() throws MessageBusException, MessageBusInitialException {
        try {
            initialize(getBusAddress());
            JMSManager.getInstance().getConnection(getBusAddress()).start();
            Session session = JMSManager.getInstance().getSession(getBusAddress());
            Topic topic = session.createTopic(getSpecificTopic());
            consumer = session.createConsumer(topic);
            consumer.setMessageListener(getJmsNativeListener());
        } catch (JMSException e) {
            logger.error("Failed to start listener!", e);
            e.printStackTrace();
            throw new MessageBusException(e);
        } catch (MessageBusInitialException e) {
            logger.error("Failed to initial!", e);
            e.printStackTrace();
            throw e;
        }
    }

    private void initialize(String busurl) throws MessageBusInitialException {
        setBusAddress(busurl);
        JMSManager.getInstance().initialJMS(busurl, getMessageBusType());
    }

    protected abstract String getSpecificTopic();

    private MessageListener getJmsNativeListener() {
        return message -> fireBusinessListener(message);
    }


    public void close() throws MessageBusException {
        try {
            if (consumer != null) {
                consumer.close();
            }
        } catch (JMSException e) {
            logger.error("Failed to close consumer!", e);
            e.printStackTrace();
            throw new MessageBusException(e);
        }
    }
}
