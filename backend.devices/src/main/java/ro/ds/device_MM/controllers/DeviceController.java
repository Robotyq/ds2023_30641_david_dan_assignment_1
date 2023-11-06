package ro.ds.device_MM.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.ds.device_MM.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.ds.device_MM.dtos.DeviceDTO;
import ro.ds.device_MM.entities.User;
import ro.ds.device_MM.services.DeviceService;
import ro.ds.device_MM.services.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/device")
public class DeviceController {

    private final DeviceService deviceService;
    private final UserService userService;

    @Autowired
    public DeviceController(DeviceService deviceService, UserService userService) {
        this.deviceService = deviceService;
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<DeviceDTO>> getDevices() {
        List<DeviceDTO> dtos = deviceService.findDevices();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping(value = "/healthCheck")
    public ResponseEntity<String> check() {
        return new ResponseEntity<>("Device Microsystem is up", HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DeviceDTO> getDevice(@PathVariable("id") UUID deviceId) {
        DeviceDTO dto = deviceService.findDeviceById(deviceId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping(value = "/getByUser/{userId}")
    public ResponseEntity<List<DeviceDTO>> getUserDevices(@PathVariable("userId") UUID userId) {
        List<DeviceDTO> dto = deviceService.findDevices(userId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("/{deviceId}/setUser")
    public ResponseEntity<UUID> setUser(@PathVariable("deviceId") UUID deviceId, @RequestParam("id") UUID userId) {
        deviceService.setUser(deviceId, userId);
        return new ResponseEntity<>(userId, HttpStatus.OK);
    }

    @PostMapping(value = "/insert")
    public ResponseEntity<UUID> insertDevice(@Valid @RequestBody DeviceDTO deviceDTO) {
        UUID deviceID = deviceService.insert(deviceDTO);
        return new ResponseEntity<>(deviceID, HttpStatus.CREATED);
    }


    @PutMapping(value = "/update")
    public ResponseEntity<UUID> updateDevice(@Valid @RequestBody DeviceDTO deviceDTO) {
        UUID deviceID = deviceService.update(deviceDTO);
        return new ResponseEntity<>(deviceID, HttpStatus.OK);
    }


    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<UUID> deleteDevice(@PathVariable("id") UUID deviceId) {
        try {
            deviceService.deleteById(deviceId);
            return new ResponseEntity<>(deviceId, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(deviceId, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/insert/user")
    public ResponseEntity<UUID> insertUser(@Valid @RequestBody User user) {
        UUID userId = userService.insert(user);
        return new ResponseEntity<>(userId, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/delete/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") String userId) {
        UUID id = UUID.fromString(userId);
        userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
