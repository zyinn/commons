package com.sumscope.optimus.commons.websocket;

import com.sumscope.optimus.commons.exceptions.BusinessRuntimeException;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeExceptionType;
import com.sumscope.optimus.commons.log.LogManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * WebSocket 管理类, 实现了发送接口和 Session管理接口
 *
 *
 * Created by qikai.yu on 2016/4/29.
 */
@Component
public class WebSocketManager implements WebSocketSender , WebSocketSessionManager{
    private static final Log logger = LogFactory.getLog(WebSocketManager.class);
    private Timer timer;

    private ConcurrentHashMap<WebSocketSession,String> webSocketClients = new ConcurrentHashMap();

    public WebSocketManager() {
//        默认状态是隔50秒发送一次，应为一般timeout时间是60s
        websocketInit(50000);
    }

    public WebSocketManager(long pingPongInterval) {
        websocketInit(pingPongInterval);

    }

    private void websocketInit(long pingPongInterval){
        timer = new Timer();
//        10秒后开始发送ping pong数据，保持WebSocket链接
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                LogManager.debug("send Ping message to clients");
                for(WebSocketSession session : webSocketClients.keySet()){
                    try {
                        session.sendMessage(formatContent("ping"));
                    } catch (IOException e) {
                        LogManager.error(new BusinessRuntimeException(BusinessRuntimeExceptionType.WEB_SOCKET_ERROR, e));
                    }
                }

            }
        },10000,pingPongInterval);
    }




    /**
     * 获取所有 websocketInit session, 直接发送
     * @param message
     */
    public void sendToAllClient(String message){
        LogManager.debug(logger, "Will send message to client , clients:" + webSocketClients.size() + ", content:" + message);
        for(WebSocketSession session : webSocketClients.keySet()){
            try {
                session.sendMessage(formatContent(message));
            } catch (IOException e) {
                LogManager.error(new BusinessRuntimeException(BusinessRuntimeExceptionType.WEB_SOCKET_ERROR, e));
            }
        }
    }

    @Override
    public int webSocketCount() {
        return webSocketClients.size();
    }

    private WebSocketMessage formatContent(String message){
        return new TextMessage(message);
    }

    /**
     * 断开连接将 session 从系统中清除
     * @param session
     */
    @Override
    public void removeSession(WebSocketSession session) {
        webSocketClients.remove(session);
    }

    /**
     * 新连接到达时, 添加 session
     * @param session
     */
    @Override
    public void addSession(WebSocketSession session) {
        webSocketClients.put(session,"");
    }
}
