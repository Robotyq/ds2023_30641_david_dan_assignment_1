package ro.ds.monitoring_MM.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.ds.monitoring_MM.entities.Measurement;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

public interface MeasurementRepository extends JpaRepository<Measurement, UUID> {

    /**
     * Example: JPA generate Query by Field
     */
    List<Measurement> findByDeviceId(UUID deviceId);

    @Query(value = "SELECT m " +
            "FROM Measurement m " +
            "WHERE m.device.id = :deviceId " +
            "AND CAST(m.timestamp AS DATE) = :date " +
            "ORDER BY m.timestamp ASC")
    List<Measurement> findByDeviceAndDate(@Param("deviceId") UUID deviceId, @Param("date") Date date);
}
