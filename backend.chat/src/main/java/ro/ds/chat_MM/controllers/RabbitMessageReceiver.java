//package ro.ds.monitoring_MM.controllers;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import ro.ds.monitoring_MM.dtos.DeviceDeleteMessage;
//import ro.ds.monitoring_MM.dtos.DeviceUpdateMessage;
//import ro.ds.monitoring_MM.dtos.MeasureJSON;
//import ro.ds.monitoring_MM.dtos.builders.MeasurementBuilder;
//import ro.ds.monitoring_MM.entities.Device;
//import ro.ds.monitoring_MM.entities.Measurement;
//import ro.ds.monitoring_MM.services.MeasurementService;
//
//import java.util.UUID;
//
//@Component
//public class RabbitMessageReceiver {
//
//    public static final String DEVICE_CHANGE_QUEUE = "deviceChangeQueue";
//    public static final String MEASURE_QUEUE = "measurementsQueue";
//    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMessageReceiver.class);
//    private final MeasurementService measurementService;
//    private final ObjectMapper objectMapper;
//
//    @Autowired
//    public RabbitMessageReceiver(MeasurementService measurementService, ObjectMapper objectMapper) {
//        this.measurementService = measurementService;
//        this.objectMapper = objectMapper;
//    }
//
//    @RabbitListener(queues = DEVICE_CHANGE_QUEUE)
//    public void receiveMessage(String message) {
//        LOGGER.debug("Message received: {}", message);
//        try {
//            DeviceUpdateMessage updateMessage = objectMapper.readValue(message, DeviceUpdateMessage.class);
//            if (updateMessage.getDevice_id() != null) {
//                Device updatedDevice = new Device(updateMessage.getDevice_id(), updateMessage.getMaxConsumption());
//                measurementService.insertOrUpdate(updatedDevice);
//            } else {
//                DeviceDeleteMessage deleteMessage = objectMapper.readValue(message, DeviceDeleteMessage.class);
//                UUID id = deleteMessage.getDeleted_id();
//                measurementService.delete(id);
//            }
//        } catch (JsonProcessingException ex) {
//            throw new RuntimeException(ex);
//        }
//    }
//
//    @RabbitListener(queues = MEASURE_QUEUE)
//    public void receiveMeasure(String message) {
//        LOGGER.info("Message received: {}", message);
//        try {
//            MeasureJSON dto = objectMapper.readValue(message, MeasureJSON.class);
//            Measurement measurement = MeasurementBuilder.getMeasurement(dto);
//            measurementService.insert(measurement);
//        } catch (JsonProcessingException ex) {
//            ex.printStackTrace();
//        }
//    }
//
//
//}
//
