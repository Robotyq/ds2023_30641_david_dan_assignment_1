package ro.ds.device_MM.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.ds.device_MM.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.ds.device_MM.dtos.DeviceDTO;
import ro.ds.device_MM.dtos.builders.DeviceBuilder;
import ro.ds.device_MM.entities.Device;
import ro.ds.device_MM.repositories.DeviceRepository;
import ro.ds.device_MM.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeviceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);
    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository, UserRepository userRepository) {
        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
    }

    public List<DeviceDTO> findDevices() {
        List<Device> deviceList = deviceRepository.findAll();
        return deviceList.stream()
                .map(DeviceBuilder::toDeviceDTO)
                .collect(Collectors.toList());
    }

    public List<DeviceDTO> findDevices(UUID userId) {
        List<Device> deviceList = deviceRepository.findByUserId(userId);
        return deviceList.stream()
                .map(DeviceBuilder::toDeviceDTO)
                .collect(Collectors.toList());
    }

    public DeviceDTO findDeviceById(UUID id) {
        Optional<Device> deviceOptional = deviceRepository.findById(id);
        if (deviceOptional.isEmpty()) {
            LOGGER.error("Device with id {} was not found in DB", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        return DeviceBuilder.toDeviceDTO(deviceOptional.get());
    }

    public UUID insert(DeviceDTO deviceDTO) {
        Device device = DeviceBuilder.toEntity(deviceDTO);
        device = deviceRepository.save(device);
        LOGGER.debug("Device with id {} was inserted in db", device.getId());
        return device.getId();
    }

    public UUID update(DeviceDTO deviceDTO) {
        UUID id = deviceDTO.getId();
        boolean deviceOptional = deviceRepository.existsById(id);
        if (!deviceOptional) {
            LOGGER.error("Device with id {} was not found in DB", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        Device device = DeviceBuilder.toEntity(deviceDTO);
        device = deviceRepository.save(device);
        LOGGER.debug("Device with id {} was updated in DB", device.getId());
        return device.getId();
    }

    public void deleteById(UUID id) {
        if (deviceRepository.existsById(id)) {
            deviceRepository.deleteById(id);
            LOGGER.debug("Device with id {} was deleted from DB", id);
            return;
        }
        LOGGER.error("Device with id {} was not found in DB", id);
        throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
    }

    public void setUser(UUID deviceId, UUID userId) {
        if (deviceRepository.existsById(deviceId)) {
            if (userRepository.existsById(userId)) {
                deviceRepository.setUser(userId, deviceId);
                LOGGER.debug("Device with id {} is now owned by User with id {}", deviceId, userId);
                return;
            }
            LOGGER.error("User with id {} was not found in DB", userId);
            throw new ResourceNotFoundException("User with id: " + userId);
        }
        LOGGER.error("Device with id {} was not found in DB", deviceId);
        throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + deviceId);
    }

}
