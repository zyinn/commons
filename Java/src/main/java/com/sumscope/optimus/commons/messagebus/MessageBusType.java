package com.sumscope.optimus.commons.messagebus;

/**
 * Created by fan.bai on 2016/3/31.
 * Indicates Message Bus type
 */
public enum MessageBusType {
    /**
     * active MQ message bus
     */
    ACTIVEMQ,

    /**
     * Informatica UM
     */
    UM,

    /**
     * Qpid
     */
    QPID
}
