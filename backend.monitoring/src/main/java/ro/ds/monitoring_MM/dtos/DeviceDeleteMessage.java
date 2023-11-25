package ro.ds.monitoring_MM.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@JsonInclude
@NoArgsConstructor
public class DeviceDeleteMessage {
    @JsonProperty
    @Getter
    private UUID deleted_id;

    @Override
    public String toString() {
        return "DeviceDeleteMessage[" + deleted_id + ']';
    }
}
