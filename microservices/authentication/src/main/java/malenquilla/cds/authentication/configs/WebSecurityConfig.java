package malenquilla.cds.authentication.configs;

import lombok.RequiredArgsConstructor;
import malenquilla.cds.authentication.enums.ApiUrl;
import malenquilla.cds.authentication.services.impl.AccountDetailsServiceImpl;
import malenquilla.cds.authentication.utils.AuthTokenFilter;
import malenquilla.cds.authentication.utils.JwtUtils;
import malenquilla.cds.common.enums.ECookies;
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
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

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
        return http.csrf(AbstractHttpConfigurer::disable)
                   .exceptionHandling(exception -> exception.authenticationEntryPoint(this.authEntryPoint()))
                   .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                   .authorizeHttpRequests(authorizeRequests ->
                           authorizeRequests.requestMatchers(ApiUrl.WHITELIST).permitAll()
                                            .anyRequest().authenticated()
                   )
                   .logout((logout) -> logout.logoutUrl(ApiUrl.LOG_OUT)
                                             .permitAll()
                                             .invalidateHttpSession(true)
                                             .clearAuthentication(true)
                                             .deleteCookies(ECookies.REFRESH_TOKEN, ECookies.ACCESS_TOKEN)
                                             .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()))
                   .authenticationProvider(this.daoAuthenticationProvider())
                   .addFilterBefore(this.authTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                   .build();
    }
}
