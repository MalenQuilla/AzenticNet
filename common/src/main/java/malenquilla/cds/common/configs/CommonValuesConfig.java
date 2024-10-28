package malenquilla.cds.common.configs;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class CommonValuesConfig {
    public static final String RUNTIME_DEVELOP = "develop";
    public static final String RUNTIME_STAGING = "staging";
    public static final String RUNTIME_LIVE = "live";

    private final String logoutUri;
    private final String environment;

    private final String grpcAuthHost;
    private final int grpcAuthPort;

    public CommonValuesConfig(
            @Value("${cds.logout.uri}") String logoutUri,
            @Value("${cds.environment}") String environment,
            
            @Value("${cds.grpc.authentication.host}") String grpcAuthHost,
            @Value("${cds.grpc.authentication.port}") int grpcAuthPort
    ) {
        this.logoutUri = logoutUri;
        this.environment = environment;
        this.grpcAuthHost = grpcAuthHost;
        this.grpcAuthPort = grpcAuthPort;
    }

    public boolean isDevelop() {
        return environment.equals(RUNTIME_DEVELOP);
    }

    public boolean isStaging() {
        return environment.equals(RUNTIME_STAGING);
    }

    public boolean isLive() {
        return environment.equals(RUNTIME_LIVE);
    }
}
