package ro.ds.user_MM.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class IndexController {

    @GetMapping(value = "/")
    public ResponseEntity<String> getStatus() {
        return new ResponseEntity<>("Users microservice is running...", HttpStatus.OK);
    }

    @GetMapping(value = "/secured")
    public ResponseEntity<String> getSecuredStatus() {
        return new ResponseEntity<>("You are allowed to access Users microservice", HttpStatus.OK);
    }
}
