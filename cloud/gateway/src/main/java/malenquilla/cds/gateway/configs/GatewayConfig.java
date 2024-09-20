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
                              route.path("/api/v1/auth/**")
                                   .filters(f -> f.filter(this.authenticationFilter()))
                                   .uri("lb://auth-service"))
                      .route("user-service", route ->
                              route.path("/api/v1/users/**")
                                   .filters(f -> f.filter(this.authenticationFilter()))
                                   .uri("lb://user-service"))
                      .build();
    }
}
