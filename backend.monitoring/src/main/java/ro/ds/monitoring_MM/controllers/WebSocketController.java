//package ro.ds.monitoring_MM.controllers;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.DestinationVariable;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.stereotype.Controller;
//import ro.ds.monitoring_MM.services.WebsocketService;
//
//import java.util.Objects;
//import java.util.UUID;
//
//@Controller
//public class WebSocketController {
//
//    @Autowired
//    private WebsocketService socketService;
//
//    @MessageMapping("/userChat/{userId}")
//    public void handleUserMessage(@DestinationVariable String userId,
//                                  @Payload String message) {
//        System.out.println("Received message from user " + userId + ": " + message);
//        if (!Objects.equals(message, "TYPE") && !Objects.equals(message, "SEEN"))
//            socketService.messageUser(UUID.fromString(userId), message);
//        socketService.messageAdmin(UUID.fromString(userId), message);
//    }
//
//    @MessageMapping("/adminChat/{userId}")
//    public void handleAdminMessage(@DestinationVariable String userId,
//                                   @Payload String message) {
//
//        System.out.println("Received message from admin for user " + userId + ": " + message);
//        socketService.messageUser(UUID.fromString(userId), message);
//        if (!Objects.equals(message, "TYPE") && !Objects.equals(message, "SEEN"))
//            socketService.messageAdmin(UUID.fromString(userId), message);
//    }
//}
