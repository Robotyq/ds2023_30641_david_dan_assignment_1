package ro.ds.device_MM.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ro.ds.device_MM.entities.Device;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {

    /**
     * Example: JPA generate Query by Field
     */
    List<Device> findByUserId(UUID userId);

    /**
     * Example: Write Custom Query
     */
    @Query(value = "SELECT d " +
            "FROM Device d " +
            "WHERE d.description LIKE :descr ")
    Optional<Device> findDeviceByAproxDescription(@Param("descr") String aproxDescr);

    /**
     * Example: Write Custom Query
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE Device " +
            "SET userId = :userId " +
            "WHERE id = :deviceId")
    void setUser(@Param("userId") UUID userId, @Param("deviceId") UUID deviceId);

}
