package malenquilla.cds.authentication.configs.grpc;

import io.grpc.BindableService;
import lombok.RequiredArgsConstructor;
import malenquilla.cds.authentication.controllers.grpc.AuthenticationControllerGrpcImpl;
import malenquilla.cds.common.grpc.servers.GrpcServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Add more grpc controllers here to init grpc server
 */
@Configuration
@RequiredArgsConstructor
public class GrpcServerConfig {
    private final AuthenticationControllerGrpcImpl authenticationControllerGrpc;

    @Bean
    public GrpcServer grpcServer() {
        List<BindableService> controllers = new ArrayList<>();
        controllers.add(this.authenticationControllerGrpc);

        return new GrpcServer(controllers);
    }
}
