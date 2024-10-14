package malenquilla.cds.authentication.configs;

import lombok.RequiredArgsConstructor;
import malenquilla.cds.authentication.services.impl.AccountDetailsServiceImpl;
import malenquilla.cds.authentication.utils.AuthTokenFilter;
import malenquilla.cds.authentication.utils.JwtUtils;
import malenquilla.cds.common.security.CommonAuthEntryPoint;
import malenquilla.cds.common.utils.CookiesUtils;
import malenquilla.cds.common.utils.RuntimeEnvUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final AccountDetailsServiceImpl accountDetailsServiceImpl;

    @Bean
    public RuntimeEnvUtils runtimeEnvUtils() {
        return new RuntimeEnvUtils();
    }

    @Bean
    public CookiesUtils cookiesUtils() {
        return new CookiesUtils(this.runtimeEnvUtils());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }

    @Bean
    public CommonAuthEntryPoint authEntryPoint() {
        return new CommonAuthEntryPoint();
    }

    @Bean
    public AuthTokenFilter authTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(this.accountDetailsServiceImpl);
        authProvider.setPasswordEncoder(this.passwordEncoder());

        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling(exception -> exception.authenticationEntryPoint(this.authEntryPoint()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorizeRequests ->
                    authorizeRequests.requestMatchers(
                                             "/api/v1/auth/login",
                                             "/api/v1/auth/request-activation/**",
                                             "/api/v1/auth/logout",
                                             "/api/v1/auth/refresh"
                                     )
                                     .permitAll()
                                     .anyRequest().authenticated()
            );

        http.authenticationProvider(this.daoAuthenticationProvider());

        http.addFilterBefore(this.authTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
