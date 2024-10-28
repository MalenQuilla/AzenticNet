package malenquilla.cds.authentication.configs;

import lombok.RequiredArgsConstructor;
import malenquilla.cds.authentication.services.impl.AccountDetailsServiceImpl;
import malenquilla.cds.authentication.utils.AuthTokenFilter;
import malenquilla.cds.authentication.utils.JwtUtils;
import malenquilla.cds.common.configs.CommonValuesConfig;
import malenquilla.cds.common.enums.ECookies;
import malenquilla.cds.common.security.CommonAuthEntryPoint;
import malenquilla.cds.common.utils.CookiesUtils;
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
    private final ValuesConfig valuesConfig;
    private final CommonValuesConfig commonValuesConfig;

    @Bean
    public CookiesUtils cookiesUtils() {
        return new CookiesUtils(this.commonValuesConfig);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtUtils jwtUtils(ValuesConfig valuesConfig) {
        return new JwtUtils(valuesConfig);
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
                           authorizeRequests.requestMatchers(this.valuesConfig.getWhitelist()).permitAll()
                                            .anyRequest().authenticated()
                   )
                   .logout((logout) -> logout.logoutUrl(this.commonValuesConfig.getLogoutUri())
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
