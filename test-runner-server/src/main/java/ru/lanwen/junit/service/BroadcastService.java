package ru.lanwen.junit.service;

import org.apache.camel.Handler;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.lanwen.junit.entity.Testcase;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service("broadcaster")
public class BroadcastService {

    private List<Session> sessions = new ArrayList<>();   //TODO поменять на конкурентное

    public void addSession(Session session) {
        sessions.add(session);
    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }
    
    @Handler
    public void broadcast(Testcase message) {
        LoggerFactory.getLogger(getClass()).info("Message broadcasted {}", message);
        
        sessions.forEach(session -> {
            try {
                session.getBasicRemote().sendObject(message);
            } catch (IOException e) {
                throw new RuntimeException("", e);
            } catch (EncodeException e) {
                throw new RuntimeException("", e);
            }
        });
    }
}
