package ro.ds.device_MM.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.ds.device_MM.entities.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

}
