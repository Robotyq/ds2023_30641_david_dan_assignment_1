package ro.ds.user_MM.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ds.user_MM.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.ds.user_MM.entities.User;
import ro.ds.user_MM.entities.UserRole;
import ro.ds.user_MM.repositories.UserInDeviceMSRepo;
import ro.ds.user_MM.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserInDeviceMSRepo userInDeviceMSRepo;

    @Autowired
    public UserService(UserRepository userRepository, UserInDeviceMSRepo userInDeviceMSRepo) {
        this.userRepository = userRepository;
        this.userInDeviceMSRepo = userInDeviceMSRepo;
    }

    public List<User> findUsers() {
        return userRepository.findAll();
    }

    public User findUserById(UUID id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            LOGGER.error("Person with id {} was not found in db", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }
        return userOptional.get();
    }

    @Transactional
    public UUID insert(User user) {
        user = userRepository.save(user);
        LOGGER.info("User with id {} was inserted in db", user.getId());

        if (userInDeviceMSRepo.insertUser(user.getId())) {
            LOGGER.info("User with id {} was inserted in DB from Devices Microservice", user.getId());
        } else {
            LOGGER.error("User with id {} was not inserted in DB from Devices Microservice", user.getId());
            userRepository.deleteById(user.getId());
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + user.getId());
        }
        return user.getId();
    }

    public UUID update(User user) {
        UUID id = user.getId();
        boolean userExists = userRepository.existsById(id);
        if (!userExists) {
            LOGGER.error("User with id {} was not found in DB", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }
        user = userRepository.save(user);
        LOGGER.info("User with id {} was updated in DB", user.getId());
        return user.getId();
    }

    @Transactional
    public void deleteById(UUID id) {
        if (userRepository.existsById(id)) {
            if (userInDeviceMSRepo.deleteUser(id)) {
                LOGGER.info("User with id {} was deleted from DB from Devices Microservice", id);
            } else {
                LOGGER.error("User with id {} was not deleted from DB from Devices Microservice", id);
                throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
            }
            userRepository.deleteById(id);
            LOGGER.info("User with id {} was deleted from DB", id);
            return;
        }
        LOGGER.error("User with id {} was not found in DB", id);
        throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
    }

    public UserRole getRole(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmailAndPass(email, password).stream().findAny();
        if (optionalUser.isEmpty()) {
            LOGGER.error("User with email {} and password {} was not found in DB", email, password);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with email: " + email + " and password: " + password);
        }
        return optionalUser.get().getRole();
    }

    public User getUser(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmailAndPass(email, password).stream().findAny();
        if (optionalUser.isEmpty()) {
            LOGGER.error("User with email {} and password {} was not found in DB", email, password);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with email: " + email + " and password: " + password);
        }
        return optionalUser.get();
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format("User with email %s not found", username)));
    }
}
