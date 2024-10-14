package malenquilla.cds.user.configs;

import malenquilla.cds.common.configs.CommonWebSecurityConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Import(value = {CommonWebSecurityConfig.class})
public class WebSecurityConfig {
}
