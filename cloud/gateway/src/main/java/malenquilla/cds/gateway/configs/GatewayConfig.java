package malenquilla.cds.gateway.configs;


import malenquilla.cds.gateway.security.AuthenticationFilter;
import malenquilla.cds.gateway.security.BroadcastFilter;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableHystrix
public class GatewayConfig {
    private final DiscoveryClient discoveryClient;

    public GatewayConfig(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Bean
    @RefreshScope
    public AuthenticationFilter authenticationFilter() {
        return new AuthenticationFilter();
    }

    @Bean
    public BroadcastFilter broadcastFilter() {
        return new BroadcastFilter(this.webClientBuilder(), this.discoveryClient);
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
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
                                   .uri("lb://authentication"))
                      .route("user-service", route ->
                              route.path("/api/v1/users/**")
                                   .filters(f -> f.filter(this.authenticationFilter()))
                                   .uri("lb://user"))
                      .route("ai-service", route ->
                              route.path("/api/v1/ai/**")
                                   .filters(f -> f.filter(this.authenticationFilter()))
                                   .uri("lb://ai"))
                      .route("logout", route ->
                              route.path("/api/v1/logout")
                                   .filters(f -> f.filter(this.broadcastFilter()))
                                   .uri("no://op"))
                      .build();
    }
}
