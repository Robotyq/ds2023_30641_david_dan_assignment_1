package ro.ds.chat_MM.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.ds.chat_MM.services.ChatService;

import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/measurement")
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping(value = "/start-chat/{user_id}")
    public ResponseEntity<String> startChat(@PathVariable("user_id") UUID userId) {
        chatService.startChat(userId);
        return new ResponseEntity<>("Admins notified", HttpStatus.OK);
    }
}
