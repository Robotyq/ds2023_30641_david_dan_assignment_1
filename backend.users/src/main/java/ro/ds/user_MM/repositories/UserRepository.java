package ro.ds.user_MM.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.ds.user_MM.entities.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Query(value = "SELECT u " +
            "FROM User u " +
            "WHERE u.email = :email " +
            "AND u.password= :pass " +
            "ORDER BY u.id LIMIT 1")
    Optional<User> findByEmailAndPass(@Param("email") String email, @Param("pass") String pass);

    Optional<User> findByEmail(String username);
}
