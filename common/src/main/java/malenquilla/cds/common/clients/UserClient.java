package malenquilla.cds.common.clients;

import malenquilla.cds.common.dtos.user.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class UserClient {

    @Value("${cds.user.client.uri}")
    private String userURI;

    private final RestTemplate restTemplate;

    public UserClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserDTO getUserByUserId(Long userId) {
        ResponseEntity<UserDTO> response = this.restTemplate.getForEntity(userURI + userId, UserDTO.class);
        assert response.getStatusCode() == HttpStatus.OK;

        return response.getBody();
    }
}
