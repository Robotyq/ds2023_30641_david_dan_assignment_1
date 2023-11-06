package ro.ds.device_MM.dtos;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
public class DeviceDTO {
    private UUID id;
    private String description;
    private String address;
    private int maxConsumption;
    private UUID userId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getMaxConsumption() {
        return maxConsumption;
    }

    public void setMaxConsumption(int maxConsumption) {
        this.maxConsumption = maxConsumption;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeviceDTO deviceDTO)) return false;
        return maxConsumption == deviceDTO.maxConsumption && id.equals(deviceDTO.id) && Objects.equals(description, deviceDTO.description) && Objects.equals(address, deviceDTO.address) && Objects.equals(userId, deviceDTO.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, address, maxConsumption, userId);
    }
}
