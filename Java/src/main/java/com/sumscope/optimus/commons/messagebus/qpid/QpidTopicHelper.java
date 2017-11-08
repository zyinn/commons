package com.sumscope.optimus.commons.messagebus.qpid;

/**
 * Created by fan.bai on 2016/4/1.
 * Qpid总线使用特殊的topic字符串，本类用于生成该字符串
 */
public class QpidTopicHelper {
    public static String getQpidTopic(String topic){
        return topic + ";{create:always, node:{type:topic,x-declare:{autodelete:true}}}";
    }

}
