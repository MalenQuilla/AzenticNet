package malenquilla.cds.gateway.security;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.RequestPath;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class BroadcastFilter implements GatewayFilter {
    private final WebClient.Builder webClientBuilder;
    private final DiscoveryClient discoveryClient;

    private static List<String> EXCLUDED_SERVICES;
    private final List<String> extraExcluded = new ArrayList<>();

    @Value("${cds.gateway.excluded.broadcast}")
    private void setExcludedPorts(String excludedPorts) {
        EXCLUDED_SERVICES = Arrays.stream(excludedPorts.split(","))
                                  .toList();
    }

    public BroadcastFilter withDefaults() {
        extraExcluded.clear();
        return this;
    }

    public BroadcastFilter excludes(String... services) {
        this.extraExcluded.addAll(List.of(services));
        return this;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        List<String> serviceIds = this.discoveryClient.getServices()
                                                      .stream()
                                                      .filter(s -> !(EXCLUDED_SERVICES.contains(s)
                                                              || extraExcluded.contains(s)))
                                                      .toList();

        List<ServiceInstance> serviceInstances = serviceIds.stream()
                                                           .map(this.discoveryClient::getInstances)
                                                           .flatMap(Collection::stream)
                                                           .toList();

        List<URI> uris = serviceInstances.stream()
                                         .map(ServiceInstance::getUri)
                                         .toList();

        MultiValueMap<String, String> cookies = new LinkedMultiValueMap<>();
        exchange.getRequest().getCookies().forEach(((s, httpCookies) -> cookies.add(s, httpCookies.toString())));

        RequestPath requestPath = exchange.getRequest().getPath();
        HttpHeaders headers = exchange.getRequest().getHeaders();
        Flux<DataBuffer> body = exchange.getRequest().getBody().cache();

        return Mono.when(uris.stream()
                             .map((uri) -> this.forwardRequest(uri, requestPath, cookies, headers, body))
                             .toList())
                   .then(chain.filter(exchange));
    }

    private Mono<Void> forwardRequest(
            URI serviceUri,
            RequestPath requestPath,
            MultiValueMap<String, String> cookieMap,
            HttpHeaders headers,
            Flux<DataBuffer> body
                                     ) {
        return this.webClientBuilder.build()
                                    .post()
                                    .uri(String.format("%s%s", serviceUri, requestPath))
                                    .cookies((cookies) -> cookies.addAll(cookieMap))
                                    .headers((httpHeaders) -> httpHeaders.addAll(headers))
                                    .body(BodyInserters.fromDataBuffers(body))
                                    .retrieve()
                                    .bodyToMono(Void.class);
    }
}
