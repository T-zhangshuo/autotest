package com.zhangshuo.autotest.service;

import com.zhangshuo.autotest.appium.AppManager;
import com.zhangshuo.autotest.appium.ServerManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/autotest/ws/{sid}")
@Component
@Slf4j
public class WebSocketServer {

    private String sid;


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        this.sid = sid;
        ServerManager.get().addServer(sid, session);
        log.info("有新窗口开始监听:" + sid + ",当前在线人数为 " + ServerManager.get().size());
        try {
            sendMessage(sid, "连接成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        ServerManager.get().removeServer(sid);
        log.info(sid + " 连接关闭！当前在线人数为 " + ServerManager.get().size());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来自窗口 " + sid + " 的信息:" + message+"  当前在线人数为 "+ServerManager.get().size());
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */

    public void sendMessage(@Nullable String sid, String message) throws IOException {
        log.info("推送消息到窗口" + sid + "，推送内容:" + message);
        ServerManager.get().sendMessage(sid, message);
    }
}