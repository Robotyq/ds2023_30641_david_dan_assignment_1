//package ro.ds.monitoring_MM.controllers;
//
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.stereotype.Controller;
//
//@Controller
//public class WebSocketController {
//
////    @MessageMapping("/send-notification")
////    @SendTo("/topic/notification")
////    public String sendNotification(String message) {
////        return message;
////    }
//
//    @MessageMapping("/sendMessage")
//    public void receiveMessage(@Payload String textMessageDTO) {
//        // receive message from client
//    }
//
//    @SendTo("/topic/message")
//    public String broadcastMessage(@Payload String textMessageDTO) {
//        return textMessageDTO;
//    }
//
////    @Autowired
////    SimpMessagingTemplate template;
////
////    @PostMapping("/send")
////    public ResponseEntity<Void> sendMessage(@RequestBody String textMessageDTO) {
////        template.convertAndSend("/topic/message", textMessageDTO);
////        return new ResponseEntity<>(HttpStatus.OK);
////    }
//}
