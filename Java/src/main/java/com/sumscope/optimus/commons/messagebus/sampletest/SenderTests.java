package com.sumscope.optimus.commons.messagebus.sampletest;

import com.sumscope.optimus.commons.messagebus.JMSManager;
import com.sumscope.optimus.commons.messagebus.MessageBusException;
import com.sumscope.optimus.commons.messagebus.MessageBusInitialException;
import com.sumscope.optimus.commons.messagebus.activemq.ActiveMQPublisher;
import com.sumscope.optimus.commons.messagebus.qpid.QpidPublisher;

/**
 * Created by fan.bai on 2016/3/31.
 * Test publish
 */
public class SenderTests {
    public static void main(String[] args) throws MessageBusException, MessageBusInitialException {
        startQpidPublisher();
//        startActiveMQListen();
//        startUMSender();
    }


    private static void startQpidPublisher() throws MessageBusException, MessageBusInitialException {
        QpidPublisher sender = new QpidPublisher("amqp://guest:guest@test/?brokerlist='tcp://172.16.86.23:5672'");
        sender.setTopic("MM.QUOTE.CREATION");
        String s = "this is a test message!";
        sender.publishBusinessMessage(s.getBytes());
        sender.close();
        JMSManager.getInstance().closeAll();

    }

    private static void startActiveMQListen() throws MessageBusInitialException, MessageBusException {
        ActiveMQPublisher sender = new ActiveMQPublisher("tcp://172.16.18.58:61616");
        sender.setTopic("BondQuote.Add");
        String s = "this is a test message!";
        sender.publishBusinessMessage(s.getBytes());
        sender.close();
        JMSManager.getInstance().closeAll();
    }
}
