package ro.ds.device_MM.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.ds.device_MM.dtos.DeviceDeleteMessage;
import ro.ds.device_MM.dtos.DeviceUpdateMessage;
import ro.ds.device_MM.services.DeviceService;

@Configuration
public class RabbitMessageSender {

    public static final String QUEUE_NAME = "deviceChangeQueue";
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);
    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMessageSender(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    @Bean
    public Queue myQueue() {
        return new Queue(QUEUE_NAME);
    }

    public void sendMessage(DeviceUpdateMessage update) {
        try {
            rabbitTemplate.convertAndSend(myQueue().getName(), objectMapper.writeValueAsString(update));
            LOGGER.info("Message sent: {}", update);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(DeviceDeleteMessage delete) {
        try {
            rabbitTemplate.convertAndSend(myQueue().getName(), objectMapper.writeValueAsString(delete));
            LOGGER.info("Message sent: {}", delete);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
