package ro.ds.user_MM.repositories;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Repository
public class UserInDeviceMSRepo {
    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${deviceBackend.ip}")
    private String deviceMSIp;

    public boolean insertUser(UUID id, String token) {
        String url = "http://" + deviceMSIp + ":8080/device/insert/user";
        User newUser = new User(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<User> request = new HttpEntity<>(newUser, headers);
        ResponseEntity<UUID> response = restTemplate.postForEntity(url, request, UUID.class);
        return response.getStatusCode().is2xxSuccessful();
    }

    public boolean deleteUser(UUID id, String token) {
        String url = "http://" + deviceMSIp + ":8080/device/delete/user/" + id.toString();
        // Create headers with Bearer token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        // Create HttpEntity with headers
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        // Make the DELETE request with headers
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                org.springframework.http.HttpMethod.DELETE,
                requestEntity,
                String.class
        );
        return response.getStatusCode().is2xxSuccessful();
    }

    @AllArgsConstructor
    private static class User implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        @Id
        @Getter
        @Setter
        private UUID id;
    }

}
