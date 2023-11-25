package ro.ds.device_MM.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@JsonInclude
public class DeviceUpdateMessage {
    @JsonProperty
    private UUID device_id;
    @JsonProperty
    private int maxConsumption;

    @Override
    public String toString() {
        return "DeviceUpdateMessage[" + maxConsumption + ", " + device_id + ']';
    }
}
