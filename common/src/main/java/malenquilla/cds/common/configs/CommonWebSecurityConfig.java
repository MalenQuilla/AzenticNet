package malenquilla.cds.common.configs;

import lombok.RequiredArgsConstructor;
import malenquilla.cds.common.grpc.clients.AuthenticationGrpcClient;
import malenquilla.cds.common.security.CommonAuthEntryPoint;
import malenquilla.cds.common.security.CommonAuthTokenFilter;
import malenquilla.cds.common.security.CommonAuthenticationProvider;
import malenquilla.cds.common.utils.CookiesUtils;
import malenquilla.cds.common.utils.RuntimeEnvUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class CommonWebSecurityConfig {
    private final AuthenticationGrpcClient authenticationGrpcClient;

    @Bean
    public RuntimeEnvUtils runtimeEnvUtils() {
        return new RuntimeEnvUtils();
    }

    @Bean
    public CookiesUtils cookiesUtils() {
        return new CookiesUtils(this.runtimeEnvUtils());
    }

    @Bean
    public CommonAuthTokenFilter authTokenFilter() {
        return new CommonAuthTokenFilter(this.authenticationGrpcClient, this.cookiesUtils());
    }

    @Bean
    public CommonAuthEntryPoint authEntryPoint() {
        return new CommonAuthEntryPoint();
    }

    @Bean
    public CommonAuthenticationProvider authenticationProvider() {
        return new CommonAuthenticationProvider();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                   .exceptionHandling(exception -> exception.authenticationEntryPoint(this.authEntryPoint()))
                   .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                   .authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
                   .authenticationProvider(this.authenticationProvider())
                   .addFilterBefore(this.authTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                   .build();
    }
}
