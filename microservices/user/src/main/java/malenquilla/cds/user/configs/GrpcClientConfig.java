package malenquilla.cds.user.configs;

import malenquilla.cds.common.configs.CommonGrpcClientConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {CommonGrpcClientConfig.class})
public class GrpcClientConfig {
}
