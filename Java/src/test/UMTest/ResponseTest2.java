package com.sumscope.optimus.commons.UMTest;

import com.latencybusters.lbm.LBM;
import com.latencybusters.lbm.LBMMessage;
import com.sumscope.ums.connection.UMConnection;
import com.sumscope.ums.constants.JacksonUtils;
import com.sumscope.ums.consum.Consumer;
import com.sumscope.ums.listener.UMMessageListener;
import com.sumscope.ums.message.Message;
import com.sumscope.ums.session.Session;
import com.sumscope.ums.topic.Topic;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

public class ResponseTest2 {
	public boolean isReceive = false;
	public boolean isReceive2 = false;
	public Session session2=null;

	@Test
	public void testResponse() throws Exception{
		
		UMConnection connection2 = new UMConnection();
		connection2.start();
		session2 = connection2.createSession();
        System.out.println("testResponse----------------");
		Topic topic = new Topic(session2.getContext(), "test");
		Consumer consumer = session2.createConsumer(topic, new UMMessageListener() {
			String targetTopic = "api.request.info";
			@Override
			public int onReceive(Object obj, LBMMessage message) {
				System.out.println("==============="+message.type());
				
				switch(message.type()){
					case LBM.MSG_DATA:
						try {
							//Message msg = MsgPackUtils.deSerializeEntity(message.data(), Message.class);
							String str = new String(message.data(),"UTF-8");
							Message msg = JacksonUtils.convertJson2Object(str, Message.class);
							System.out.println("Received "
	                                + message.dataLength()
	                                + " bytes on topic "
	                                + message.topicName()
	                                + ": '"
	                                + JacksonUtils.convertObject2Json(msg));
							//isReceive = true;
							//Message msg = JacksonUtils.convertJson2Object(new String(message.data(),"UTF-8"), Message.class);
//							String host = msg.getReplyToHost();
//							StringTokenizer tokens = new StringTokenizer(host,":");
//							if(tokens.countTokens()==2){
//								ip = tokens.nextToken();
//								port = Integer.parseInt(tokens.nextToken());
//							}
							//String tcp = String.format("TCP:%s:%s", ip,port);
//							targetTopic = msg.getReplyToTopic();
//							msg.setProperties("compress", "false");
//							//UnicastImmediateProducer producer2 = session2.createProducer(tcp, targetTopic);
//							LBMTopic topic2 = new Topic(session2.getContext(), targetTopic,new SourceAttributes());
//							Producer producer2 = session2.createProducer(topic2, new DefaultProducerEventCallBack());
							
							//port++;
						} catch (UnsupportedEncodingException e) {
							 System.out.println("This system doesn't support the UTF-8 code page.");
							 System.exit(1);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} 
						break;
					case LBM.MSG_BOS:
						System.out.println("[" + message.topicName() + "][" + message.source() + "], Beginning of Transport Session");
						break;

				    case LBM.MSG_EOS:
						System.out.println("[" + message.topicName() + "][" + message.source() + "], End of Transport Session");
						break;
		            default:
		            	break;
				}
				message.dispose();
				return 0;
			}
		});
		while(!isReceive){
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
//		isReceive = false;
			
		consumer.close();
		session2.close();
		connection2.close();
	}
	
}
