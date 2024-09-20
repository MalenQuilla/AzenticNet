package malenquilla.cds.common.clients;

import malenquilla.cds.common.exceptions.HTTPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.client.RestTemplate;

public class AuthClient {
    @Value("${cds.auth.client.uri}")
    private String authURI;

    private final RestTemplate restTemplate;

    @Autowired
    public AuthClient(
            RestTemplate restTemplate
    ) {
        this.restTemplate = restTemplate;
    }

    public UsernamePasswordAuthenticationToken verifyAuthentication(String authHeader) {
        ResponseEntity<UsernamePasswordAuthenticationToken> response =
                this.restTemplate.postForEntity(authURI + "verify", authHeader, UsernamePasswordAuthenticationToken.class);

        if (response.getStatusCode() != HttpStatus.OK)
            throw new HTTPException(response.getStatusCode());

        return response.getBody();
    }
}
