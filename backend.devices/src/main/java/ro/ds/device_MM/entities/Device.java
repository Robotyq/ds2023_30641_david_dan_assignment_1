package ro.ds.device_MM.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Entity
public class Device implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private UUID id;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "address")
    private String address;
    @Column(name = "maxConsumption")
    private int maxConsumption;

    //    @GeneratedValue(generator = "UUID")
//    @GenericGenerator(
//            name = "UUID",
//            strategy = "org.hibernate.id.UUIDGenerator"
//    )
    @Column(name = "userId", updatable = true, nullable = true, unique = false)
    @Setter
    @Getter
    private UUID userId;

    public Device() {
    }

    public Device(UUID id, String description, String address, int maxConsumption, UUID userId) {
        this.id = id;
        this.description = description;
        this.address = address;
        this.maxConsumption = maxConsumption;
        this.userId = userId;
    }

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
}
