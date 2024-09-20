package malenquilla.cds.common.configs;

import malenquilla.cds.common.clients.AuthClient;
import malenquilla.cds.common.security.CommonAuthEntryPoint;
import malenquilla.cds.common.security.CommonAuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class CommonWebSecurityConfig {
    private final AuthClient authClient;

    @Autowired
    public CommonWebSecurityConfig(
            AuthClient authClient
    ) {
        this.authClient = authClient;
    }

    @Bean
    public CommonAuthTokenFilter authTokenFilter() {
        return new CommonAuthTokenFilter(this.authClient);
    }

    @Bean
    public CommonAuthEntryPoint authEntryPoint() {
        return new CommonAuthEntryPoint();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling(exception -> exception.authenticationEntryPoint(this.authEntryPoint()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated());

        http.addFilterBefore(this.authTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
