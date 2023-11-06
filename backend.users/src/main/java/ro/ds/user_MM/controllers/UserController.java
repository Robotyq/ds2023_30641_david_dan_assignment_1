package ro.ds.user_MM.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.ds.user_MM.entities.User;
import ro.ds.user_MM.entities.UserRole;
import ro.ds.user_MM.services.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<User>> getUsers() {
        List<User> dtos = userService.findUsers();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") UUID userId) {
        User dto = userService.findUserById(userId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping(value = "/insert")
    public ResponseEntity<UUID> insertUser(@Valid @RequestBody User user) {
        UUID userId = userService.insert(user);
        return new ResponseEntity<>(userId, HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<UUID> updateUser(@Valid @RequestBody User user) {
        UUID userId = userService.update(user);
        return new ResponseEntity<>(userId, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<UUID> DeleteUser(@PathVariable("id") UUID userId) {
        userService.deleteById(userId);
        return new ResponseEntity<>(userId, HttpStatus.OK);
    }

    @GetMapping("/role")
    public ResponseEntity<UserRole> getRole(@RequestParam("email") String email, @RequestParam("pass") String pass) {
        UserRole role = userService.getRole(email, pass);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @GetMapping("/login")
    public ResponseEntity<User> login(@RequestParam("email") String email, @RequestParam("pass") String pass) {
        User user = userService.getUser(email, pass);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


}
