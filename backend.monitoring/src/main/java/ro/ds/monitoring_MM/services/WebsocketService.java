package ro.ds.monitoring_MM.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ro.ds.monitoring_MM.dtos.LiveConsumption;

@Service
public class WebsocketService {
    private final SimpMessagingTemplate socketTemplate;

    @Autowired
    public WebsocketService(SimpMessagingTemplate socketTemplate) {
        this.socketTemplate = socketTemplate;
    }

    public void sendConsumption(LiveConsumption consumptionDTO) {
        socketTemplate.convertAndSend("/topic/message", consumptionDTO.toString());
    }

}
