package ro.ds.chat_MM.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WebsocketService {
    private final SimpMessagingTemplate socketTemplate;

    @Autowired
    public WebsocketService(SimpMessagingTemplate socketTemplate) {
        this.socketTemplate = socketTemplate;
    }

    public void startChat(UUID chatId) {
        String topic = "/topic/admin";
        socketTemplate.convertAndSend(topic, "Hello, new chat started for user:" + chatId);
    }

    public void messageUser(UUID chatId, String message) {
        // Assuming your WebSocket topic structure is "/topic/user/{userId}"
        String topic = "/topic/user/" + chatId;
        // Send a message to the specified user's topic
        socketTemplate.convertAndSend(topic, message);
    }

    public void messageAdmin(UUID chatId, String message) {
        String topic = "/topic/admin/" + chatId;
        socketTemplate.convertAndSend(topic, message);
    }
}
