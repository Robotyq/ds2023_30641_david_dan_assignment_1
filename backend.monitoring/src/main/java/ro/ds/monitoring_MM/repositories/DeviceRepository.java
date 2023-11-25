package ro.ds.monitoring_MM.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.ds.monitoring_MM.entities.Device;

import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {

}
