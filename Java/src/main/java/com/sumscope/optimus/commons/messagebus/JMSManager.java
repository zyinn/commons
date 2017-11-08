package com.sumscope.optimus.commons.messagebus;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.qpid.client.AMQConnectionFactory;
import org.apache.qpid.url.URLSyntaxException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fan.bai on 2016/4/1.
 * 提供基于JMS类的管理工具。ConnectoryFactory，Connection，Session实例被存储在本实例中。请在使用完毕后调用close方法关闭
 */
public class JMSManager {
    Logger logger = LoggerFactory.getLogger(JMSManager.class);
    private static JMSManager instance = new JMSManager();
    private Map<String, JMSTrippleElement> cache;

    private JMSManager() {
        cache = new HashMap<>();
    }

    public void initialJMS(String busURL, MessageBusType type) throws MessageBusInitialException {
        if (cache.get(busURL) != null){
            return;
        }
        ConnectionFactory connectionFactory = null;
        switch (type) {
            case ACTIVEMQ:
                connectionFactory = new ActiveMQConnectionFactory(busURL);
                break;
            case UM:
                throw new RuntimeException("Only support JMS message bus!");
            case QPID:
                try {
                    connectionFactory = new AMQConnectionFactory(busURL);
                } catch (URLSyntaxException e) {
                    logger.error("Failed to initialize JMS!",e);
                    e.printStackTrace();
                    throw new MessageBusInitialException(e);
                }
                break;
        }

        try {
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            JMSTrippleElement element = new JMSTrippleElement(connectionFactory, connection, session);
            cache.put(busURL, element);

        } catch (JMSException e) {
            logger.error("Failed to initialize JMS!",e);
            e.printStackTrace();
            throw new MessageBusInitialException(e);
        }
    }

    public static JMSManager getInstance(){
        return instance;
    }

    public Connection getConnection(String busURL) {
        JMSTrippleElement jmsTrippleElement = cache.get(busURL);
        return jmsTrippleElement.getConnection();
    }

    public Session getSession(String busURL) {
        JMSTrippleElement jmsTrippleElement = cache.get(busURL);
        return jmsTrippleElement.getSession();
    }

    private class JMSTrippleElement {
        private final ConnectionFactory connectionFactory;
        private final Connection connection;
        private final Session session;

        public JMSTrippleElement(ConnectionFactory factory, Connection connection, Session session) {
            this.connectionFactory = factory;
            this.connection = connection;
            this.session = session;
        }


        public Connection getConnection() {
            return connection;
        }


        public Session getSession() {
            return session;
        }

    }

    public void close(String busURL){
        JMSTrippleElement jmsTrippleElement = cache.get(busURL);
        if(jmsTrippleElement != null){
            try {
                jmsTrippleElement.getSession().close();
                jmsTrippleElement.getConnection().close();
            } catch (JMSException e) {
                logger.error("Failed to close! " + busURL,e);
                e.printStackTrace();
            }
        }
    }

    public void closeAll(){
        cache.keySet().forEach(this::close);
    }


}
