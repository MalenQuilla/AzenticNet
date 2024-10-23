package malenquilla.cds.gateway.configs;

import malenquilla.cds.gateway.security.AuthenticationFilter;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableHystrix
public class GatewayConfig {
    @Bean
    @RefreshScope
    public AuthenticationFilter authenticationFilter() {
        return new AuthenticationFilter();
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                      .route("auth-service", route ->
                              route.path(
                                           "/api/v1/accounts/**",
                                           "/api/v1/roles/**",
                                           "/api/v1/auth/**")
                                   .filters(f -> f.filter(this.authenticationFilter()))
                                   .uri("http://localhost:7000"))
                      .route("user-service", route ->
                              route.path("/api/v1/users/**")
                                   .filters(f -> f.filter(this.authenticationFilter()))
                                   .uri("http://localhost:7001"))
                      .route("ai-service", route ->
                              route.path("/api/v1/ai/**")
                                   .filters(f -> f.filter(this.authenticationFilter()))
                                   .uri("http://localhost:7002"))
                      .build();
    }
}
