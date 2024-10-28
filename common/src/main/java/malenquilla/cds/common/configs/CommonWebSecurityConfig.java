package malenquilla.cds.common.configs;

import malenquilla.cds.common.grpc.clients.AuthenticationGrpcClient;
import malenquilla.cds.common.security.CommonAuthEntryPoint;
import malenquilla.cds.common.security.CommonAuthTokenFilter;
import malenquilla.cds.common.security.CommonAuthenticationProvider;
import malenquilla.cds.common.utils.CookiesUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;


public class CommonWebSecurityConfig {
    private final AuthenticationGrpcClient authenticationGrpcClient;
    private final HandlerExceptionResolver resolver;
    private final CommonValuesConfig commonValuesConfig;

    public CommonWebSecurityConfig(
            AuthenticationGrpcClient authenticationGrpcClient,
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver,
            CommonValuesConfig commonValuesConfig
    ) {
        this.authenticationGrpcClient = authenticationGrpcClient;
        this.resolver = resolver;
        this.commonValuesConfig = commonValuesConfig;
    }

    @Bean
    public CookiesUtils cookiesUtils() {
        return new CookiesUtils(this.commonValuesConfig);
    }

    @Bean
    public CommonAuthTokenFilter authTokenFilter() {
        return new CommonAuthTokenFilter(this.authenticationGrpcClient, this.cookiesUtils(), this.resolver);
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
                   .logout((logout) -> logout.logoutUrl(this.commonValuesConfig.getLogoutUri())
                                             .permitAll()
                                             .invalidateHttpSession(true)
                                             .clearAuthentication(true)
                                             .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()))
                   .authenticationProvider(this.authenticationProvider())
                   .addFilterBefore(this.authTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                   .build();
    }
}
