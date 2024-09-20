package malenquilla.cds.common.configs;

import malenquilla.cds.common.clients.AuthClient;
import malenquilla.cds.common.clients.UserClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CommonClientConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public UserClient userClient() {
        return new UserClient(this.restTemplate());
    }

    @Bean
    public AuthClient authClient() {
        return new AuthClient(this.restTemplate());
    }
}
