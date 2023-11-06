package ro.ds.device_MM.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.ds.device_MM.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.ds.device_MM.entities.User;
import ro.ds.device_MM.repositories.UserRepository;

import java.util.UUID;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UUID insert(User newUser) {
        UUID insertedId = userRepository.save(newUser).getId();
        LOGGER.debug("User id {} was inserted in db", insertedId);
        return insertedId;
    }

    public void deleteById(UUID id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            LOGGER.debug("User id {} was deleted from DB", id);
            return;
        }
        LOGGER.error("User id {} was not found in DB", id);
        throw new ResourceNotFoundException("User ID " + id);
    }

}
