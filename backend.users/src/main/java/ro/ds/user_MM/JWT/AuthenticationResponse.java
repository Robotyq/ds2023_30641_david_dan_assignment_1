package ro.ds.user_MM.JWT;

import ro.ds.user_MM.entities.User;

public record AuthenticationResponse(String token, User user) {
}
