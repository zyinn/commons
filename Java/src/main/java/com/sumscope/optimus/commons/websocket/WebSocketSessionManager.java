package com.sumscope.optimus.commons.websocket;

import org.springframework.web.socket.WebSocketSession;

/**
 *
 * WebSocket 管理接口
 *
 * Created by qikai.yu on 2016/4/29.
 */
public interface WebSocketSessionManager {
    void removeSession(WebSocketSession session);
    void addSession(WebSocketSession session);
}
