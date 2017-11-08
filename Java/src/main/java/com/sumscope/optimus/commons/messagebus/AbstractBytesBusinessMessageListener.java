package com.sumscope.optimus.commons.messagebus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;

/**
 * Created by fan.bai on 2016/3/31.
 * 从不同的总线消息中获取业务信息，只支持bytes数据
 */
public abstract class AbstractBytesBusinessMessageListener extends AbstractBusinessMessageListener {
    public static Logger logger = LoggerFactory.getLogger(AbstractBytesBusinessMessageListener.class);

    protected void processJMSMessage(String topic, Message sourceMessage) {
        if(sourceMessage instanceof BytesMessage){
            BytesMessage bytesM = (BytesMessage)sourceMessage;
            try {
                byte[] msg = new byte[(int) bytesM.getBodyLength()];
                bytesM.readBytes(msg);
                onBusinessMessageReceived(topic,msg);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    protected abstract void onBusinessMessageReceived(String topic, byte[] msg);
}
