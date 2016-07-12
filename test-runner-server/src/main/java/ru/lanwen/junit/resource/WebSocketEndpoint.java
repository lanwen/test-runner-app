package ru.lanwen.junit.resource;

import org.springframework.web.socket.server.standard.SpringConfigurator;
import ru.lanwen.junit.service.BroadcastService;

import javax.inject.Inject;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * @author lanwen (Merkushev Kirill)
 */
@ServerEndpoint(value = "/echo", configurator = SpringConfigurator.class)
public class WebSocketEndpoint {

    @Inject
    private BroadcastService broadcaster;

    @OnOpen
    public void onOpen(Session session) throws IOException {
        session.getBasicRemote().sendText("onOpen");
        broadcaster.addSession(session);
    }

    @OnError
    public void onError(Session session, Throwable t) {
        t.printStackTrace();
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) throws IOException {
        broadcaster.removeSession(session);
    }
}
