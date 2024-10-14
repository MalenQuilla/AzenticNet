package malenquilla.cds.authentication.configs.grpc;

import malenquilla.cds.authentication.controllers.grpc.AuthenticationControllerGrpcImpl;
import malenquilla.cds.authentication.services.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcControllerConfig {
    @Bean
    public AuthenticationControllerGrpcImpl authenticationControllerGrpc(
            AuthenticationService authenticationService
    ) {
        return new AuthenticationControllerGrpcImpl(authenticationService);
    }
}
