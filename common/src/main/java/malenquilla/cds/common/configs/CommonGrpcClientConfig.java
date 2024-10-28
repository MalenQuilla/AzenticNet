package malenquilla.cds.common.configs;

import malenquilla.cds.common.grpc.clients.AuthenticationGrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonGrpcClientConfig {
    @Bean
    public AuthenticationGrpcClient authenticationGrpcClient(CommonValuesConfig commonValuesConfig) {
        return new AuthenticationGrpcClient(commonValuesConfig);
    }
}
