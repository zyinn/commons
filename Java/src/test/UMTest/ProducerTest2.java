package com.sumscope.optimus.commons.UMTest;

import com.latencybusters.lbm.LBM;
import com.latencybusters.lbm.LBMException;
import com.sumscope.ums.attributes.SourceAttributes;
import com.sumscope.ums.connection.UMConnection;
import com.sumscope.ums.message.Message;
import com.sumscope.ums.produce.Producer;
import com.sumscope.ums.session.Session;
import com.sumscope.ums.topic.Topic;
import org.junit.Before;
import org.junit.Test;

public class ProducerTest2 {
	private Producer producer;
	UMConnection connection;
	Session session;

	@Before
	public void init() throws LBMException {
		try {
			System.out.println("init-------------");
			connection = new UMConnection();
			connection.start();
			session = connection.createSession();
			Topic topic = new Topic(session.getContext(), "test",
					new SourceAttributes());
			producer = session.createProducer(topic);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSend() throws Exception {
		System.out.println("testSend-------------");
		//String str = "{\"data\": [{\"holidayDate\": \"2018-12-30\", \"country\": \"CNY\", \"holidayReason\": \"双休日\", \"marketType\": \"SSE\"}], \"result\": \"Success\"}";
		//String json = "{\"replyToHost\": \"172.16.89.5\", \"fragment\": false, \"messageBody\": [{\"bondCode\": \"150308.IB\", \"valYield\": 3.4581, \"couponRateCurrent\": 4.29, \"corpRating\": null, \"fullName\": \"\u4e2d\u56fd\u8fdb\u51fa\u53e3\u94f6\u884c2015\u5e74\u7b2c\u516b\u671f\u91d1\u878d\u503a\u5238\", \"residualMaturity\": \"8.98Y\", \"bondRating\": null, \"issuerName\": \"\u4e2d\u56fd\u8fdb\u51fa\u53e3\u94f6\u884c\", \"shortName\": \"15\u8fdb\u51fa08\"}], \"fromGateway\": false, \"messageId\": \"c9e5481b52594e1b97bdb6ebb782bc47\", \"replyToTopic\": \"172.16.89.5-8010-apigw/c9e5481b52594e1b97bdb6ebb782bc47\", \"messagesBuffer\": null, \"properties\": {\"receiver\": \"c9e5481b52594e1b97bdb6ebb782bc47\"}}";
		Message sendMsg = new Message();
		sendMsg.setMessageBody("test");
		sendMsg.setProperties(null);
		sendMsg.setMessageId("aaaaaaaaaaaaa1");
		producer.send(sendMsg,LBM.MSG_FLUSH | LBM.SRC_BLOCK);
		System.out.println(sendMsg);
		producer.close();
		session.close();
		connection.close();

	}
}