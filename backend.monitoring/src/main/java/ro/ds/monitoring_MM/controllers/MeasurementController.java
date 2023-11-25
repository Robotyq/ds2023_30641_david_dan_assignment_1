package ro.ds.monitoring_MM.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.ds.monitoring_MM.dtos.HourMeasure;
import ro.ds.monitoring_MM.services.MeasurementService;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/measurement")
public class MeasurementController {

    private final MeasurementService measurementService;
//    private final UserService userService;

    @Autowired
    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

//    @GetMapping()
//    public ResponseEntity<List<HourMeasure>> getDevices() {
//        List<DeviceDTO> dtos = measurementService.findDevices();
//        return new ResponseEntity<>(dtos, HttpStatus.OK);
//    }

    @GetMapping(value = "/{device_id}")
    public ResponseEntity<List<HourMeasure>> getHistory(@PathVariable("device_id") UUID deviceId) {
        Date date = new Date(123, 11, 24);
        List<HourMeasure> history = measurementService.getConsumptionForDate(deviceId, date);
        return new ResponseEntity<>(history, HttpStatus.OK);
    }
//
//    @GetMapping(value = "/getByUser/{userId}")
//    public ResponseEntity<List<DeviceDTO>> getUserDevices(@PathVariable("userId") UUID userId) {
//        List<DeviceDTO> dto = measurementService.findDevices(userId);
//        return new ResponseEntity<>(dto, HttpStatus.OK);
//    }
//
//    @PutMapping("/{deviceId}/setUser")
//    public ResponseEntity<UUID> setUser(@PathVariable("deviceId") UUID deviceId, @RequestParam("id") UUID userId) {
//        measurementService.setUser(deviceId, userId);
//        return new ResponseEntity<>(userId, HttpStatus.OK);
//    }
//
//    @PostMapping(value = "/insert")
//    public ResponseEntity<UUID> insertDevice(@RequestBody DeviceDTO deviceDTO) {
//        UUID deviceID = measurementService.insert(deviceDTO);
//        return new ResponseEntity<>(deviceID, HttpStatus.CREATED);
//    }
//
//
//    @PutMapping(value = "/update")
//    public ResponseEntity<UUID> updateDevice(@RequestBody DeviceDTO deviceDTO) {
//        UUID deviceID = measurementService.update(deviceDTO);
//        return new ResponseEntity<>(deviceID, HttpStatus.OK);
//    }
//
//
//    @DeleteMapping(value = "/delete/{id}")
//    public ResponseEntity<UUID> deleteDevice(@PathVariable("id") UUID deviceId) {
//        try {
//            measurementService.deleteById(deviceId);
//            return new ResponseEntity<>(deviceId, HttpStatus.OK);
//        } catch (ResourceNotFoundException e) {
//            return new ResponseEntity<>(deviceId, HttpStatus.NOT_FOUND);
//        }
//    }
//
//    @PostMapping(value = "/insert/user")
//    public ResponseEntity<UUID> insertUser(@RequestBody Device device) {
//        UUID userId = userService.insert(device);
//        return new ResponseEntity<>(userId, HttpStatus.CREATED);
//    }
//
//    @DeleteMapping(value = "/delete/user/{id}")
//    public ResponseEntity<String> deleteUser(@PathVariable("id") String userId) {
//        UUID id = UUID.fromString(userId);
//        userService.deleteById(id);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
}
