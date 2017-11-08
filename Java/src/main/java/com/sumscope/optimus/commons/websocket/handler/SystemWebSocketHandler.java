/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sumscope.optimus.commons.websocket.handler;

import com.sumscope.optimus.commons.exceptions.*;
import com.sumscope.optimus.commons.log.LogManager;
import com.sumscope.optimus.commons.websocket.WebSocketSessionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.socket.*;

import java.io.IOException;

/**
 * WebSocket 处理类
 */
public class SystemWebSocketHandler implements WebSocketHandler {
    private static final Log logger = LogFactory.getLog(SystemWebSocketHandler.class);

    private WebSocketSessionManager manager;

    public SystemWebSocketHandler(WebSocketSessionManager wssManager){
        this.manager = wssManager;

    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        try {
            manager.addSession(session);
        } catch (Exception e) {
            LogManager.error(new BusinessRuntimeException(BusinessRuntimeExceptionType.WEB_SOCKET_ERROR , e));
        }
    }

    @Override
    public void handleMessage(WebSocketSession wss, WebSocketMessage<?> wsm) throws Exception {
        TextMessage returnMessage = new TextMessage(wsm.getPayload()
				+ " received at server");
		wss.sendMessage(returnMessage);
    }

    @Override
    public void handleTransportError(WebSocketSession wss, Throwable thrwbl) throws Exception {
        if(wss.isOpen()){
            try {
                wss.close();
            } catch (IOException e) {
                LogManager.error(new BusinessRuntimeException(BusinessRuntimeExceptionType.WEB_SOCKET_ERROR , e));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession wss, CloseStatus cs) throws Exception {
        LogManager.info(logger, wss + "is closed , due to " + cs.getReason());
        try {
            wss.close(CloseStatus.NORMAL);
            manager.removeSession(wss);
        } catch (Exception e) {
            LogManager.error(new BusinessRuntimeException(BusinessRuntimeExceptionType.WEB_SOCKET_ERROR , e));
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return true;
    }
    
}
