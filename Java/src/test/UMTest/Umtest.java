import com.latencybusters.lbm.LBMException;
import com.sumscope.optimus.commons.messagebus.MessageBusException;
import com.sumscope.optimus.commons.messagebus.MessageBusInitialException;
import com.sumscope.ums.message.Message;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by Administrator on 2016/7/4.
 */
public class Umtest {
    @Test
    public void sendMessage() throws IOException {
        UmPublisher up = new UmPublisher();
        Message sendMsg = new Message();
        sendMsg.setMessageBody("331211");
        sendMsg.setProperties(null);
        sendMsg.setMessageId("测试2");
        //String s = JsonUtil.writeValueAsString(sendMsg);
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(sendMsg);
        byte[] bytes = bo.toByteArray();
        try {
            up.publishBusinessMessage(bytes);
            System.out.println("发送成功" + sendMsg);
        } catch (MessageBusException e) {
            e.printStackTrace();
        } catch (MessageBusInitialException e) {
            e.printStackTrace();
        } catch (LBMException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMessage() {
        UmSubscriber us = new UmSubscriber();
        try {
            us.startToListen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
