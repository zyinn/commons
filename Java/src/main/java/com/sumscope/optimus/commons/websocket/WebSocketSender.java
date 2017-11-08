package com.sumscope.optimus.commons.websocket;

/**
 *
 * 给客户端发送 WebSocket 消息的接口
 *
 * Created by qikai.yu on 2016/4/29.
 */
public interface WebSocketSender {
    void sendToAllClient(String message);
    int webSocketCount();
}
