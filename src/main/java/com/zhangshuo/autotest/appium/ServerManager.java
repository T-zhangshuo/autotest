package com.zhangshuo.autotest.appium;

import com.sun.corba.se.spi.activation.ServerManagerHelper;
import com.zhangshuo.autotest.config.GlobalException;
import com.zhangshuo.autotest.service.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.TextUtils;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ServerManager {
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private Map<String, Session> webSocketMap = new ConcurrentHashMap<>();

    public void addServer(String sid, Session session) {
        webSocketMap.put(sid, session);
    }

    public void removeServer(String sid) {
        webSocketMap.remove(sid);
    }

    public int size() {
        return webSocketMap.size();
    }

    public void sendMessage(String sid, String message) throws IOException {
        if (TextUtils.isEmpty(sid)) {
            //如果为空，则为群发消息
            for (Map.Entry<String, Session> map : webSocketMap.entrySet()) {
                map.getValue().getBasicRemote().sendText(message);
            }
        } else {
            Session session = webSocketMap.get(sid);
            if (session == null) {
                throw new IOException("不存在Session");
            }
            session.getBasicRemote().sendText(message);
        }

    }

    public static ServerManager get() {
        return ServerManagerHelper.serverManager;
    }

    private ServerManager() {

    }

    public static final class ServerManagerHelper {
        static ServerManager serverManager = new ServerManager();
    }
}
