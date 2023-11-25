package ro.ds.device_MM.dtos.builders;

import ro.ds.device_MM.dtos.DeviceDTO;
import ro.ds.device_MM.dtos.DeviceUpdateMessage;
import ro.ds.device_MM.entities.Device;

public class DeviceBuilder {

    private DeviceBuilder() {
    }

    public static DeviceDTO toDeviceDTO(Device device) {
        return new DeviceDTO(device.getId(),
                device.getDescription(),
                device.getAddress(),
                device.getMaxConsumption(),
                device.getUserId());
    }

    public static Device toEntity(DeviceDTO deviceDTO) {
        return new Device(deviceDTO.getId(), deviceDTO.getDescription(), deviceDTO.getAddress(), deviceDTO.getMaxConsumption(), deviceDTO.getUserId());
    }

    public static DeviceUpdateMessage toDeviceUpdateMessage(Device device) {
        return new DeviceUpdateMessage(device.getId(), device.getMaxConsumption());
    }
}
