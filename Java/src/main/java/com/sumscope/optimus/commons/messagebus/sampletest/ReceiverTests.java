package com.sumscope.optimus.commons.messagebus.sampletest;

import com.sumscope.optimus.commons.messagebus.AbstractBytesBusinessMessageListener;
import com.sumscope.optimus.commons.messagebus.MessageBusException;
import com.sumscope.optimus.commons.messagebus.MessageBusInitialException;
import com.sumscope.optimus.commons.messagebus.activemq.ActiveMQSubscriber;
import com.sumscope.optimus.commons.messagebus.qpid.QpidSubscriber;

/**
 * Created by fan.bai on 2016/3/31.
 */
public class ReceiverTests {
    public static void main(String[] args) throws MessageBusInitialException, MessageBusException {
//        startActiveMQListen();
        startQpidListen();
//        startUMListen();


    }


    private static void startQpidListen() throws MessageBusInitialException, MessageBusException {
        QpidSubscriber receiver = new QpidSubscriber("amqp://guest:guest@test/?brokerlist='tcp://172.16.86.23:5672'");
//        receiver.setTopic("JMS2QPID.FANOUT.FULL");
        receiver.setTopic("MM.QUOTE.CREATION");
        receiver.setSubscriberListener(new AbstractBytesBusinessMessageListener() {
            @Override
            protected void onBusinessMessageReceived(String topic, byte[] msg) {
                String s = new String(msg);
                System.out.println(topic + ":" + s);
            }
        });
        receiver.startToListen();
    }

    private static void startActiveMQListen() throws MessageBusInitialException, MessageBusException {
        ActiveMQSubscriber receiver = new ActiveMQSubscriber("tcp://172.16.18.58:61616");
        receiver.setTopic("BondQuote.Add");
        receiver.setSubscriberListener(new AbstractBytesBusinessMessageListener() {
            @Override
            protected void onBusinessMessageReceived(String topic, byte[] msg) {
                String s = new String(msg);
                System.out.println(topic + ":" + s);
            }
        });
        receiver.startToListen();
    }
}
