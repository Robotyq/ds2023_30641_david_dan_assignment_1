package ro.ds.monitoring_MM.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Measurement implements Serializable {

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
    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false, updatable = false)
    private Device device;
    @Column(name = "timestamp", nullable = false)
    @Getter
    private Timestamp timestamp;
    @Column(name = "measure", nullable = false)
    @Getter
    private long measure;

}
