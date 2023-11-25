package ro.ds.monitoring_MM.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Measurement implements Serializable {

    @JsonProperty
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private UUID id;
    @JsonProperty
    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false, updatable = false)
    @Getter
    @Setter
    private Device device;
    @JsonProperty
    @Column(name = "timestamp", nullable = false)
    @Getter
    private Timestamp timestamp;
    @JsonProperty
    @Column(name = "measure", nullable = false)
    @Getter
    private double measure;

}
