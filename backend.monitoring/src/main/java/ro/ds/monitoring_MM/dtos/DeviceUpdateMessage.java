package ro.ds.monitoring_MM.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@JsonInclude
public class DeviceUpdateMessage {
    @JsonProperty
    @Getter
    private UUID device_id;
    @Getter
    @JsonProperty
    private int maxConsumption;

    @Override
    public String toString() {
        return "DeviceUpdateMessage[" + maxConsumption + ", " + device_id + ']';
    }
}
