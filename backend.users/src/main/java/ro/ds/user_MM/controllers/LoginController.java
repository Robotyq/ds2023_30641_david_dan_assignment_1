package ro.ds.user_MM.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.ds.user_MM.JWT.AuthenticationResponse;
import ro.ds.user_MM.JWT.JwtService;
import ro.ds.user_MM.entities.User;
import ro.ds.user_MM.services.UserService;

@RestController
//@CrossOrigin
@RequestMapping(value = "/user/login")
public class LoginController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public LoginController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginDTO loginDTO) {
        User user = userService.getUser(loginDTO.email, loginDTO.pass);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        String token = jwtService.generateToken(user);
        AuthenticationResponse response = new AuthenticationResponse(token, user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private record LoginDTO(String email, String pass) {
    }

}
