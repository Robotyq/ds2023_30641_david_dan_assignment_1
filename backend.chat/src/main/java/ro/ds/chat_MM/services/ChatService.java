package ro.ds.chat_MM.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ChatService {
    private final WebsocketService socketService;

    @Autowired
    public ChatService(WebsocketService socketService) {
        this.socketService = socketService;
    }

    public void startChat(UUID chatId) {
        socketService.startChat(chatId);
    }
}
