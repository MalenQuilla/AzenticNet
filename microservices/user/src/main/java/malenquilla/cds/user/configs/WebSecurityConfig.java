package malenquilla.cds.user.configs;

import malenquilla.cds.common.configs.CommonWebSecurityConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {CommonWebSecurityConfig.class})
public class WebSecurityConfig {
}
