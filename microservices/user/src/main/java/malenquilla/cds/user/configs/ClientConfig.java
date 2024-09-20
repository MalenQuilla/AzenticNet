package malenquilla.cds.user.configs;

import malenquilla.cds.common.configs.CommonClientConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {CommonClientConfig.class})
public class ClientConfig {
}
