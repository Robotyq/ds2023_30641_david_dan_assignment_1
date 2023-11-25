package ro.ds.device_MM.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@JsonInclude
public class DeviceDeleteMessage {
    @JsonProperty
    private UUID deleted_id;

    @Override
    public String toString() {
        return "DeviceDeleteMessage[" + deleted_id + ']';
    }
}
