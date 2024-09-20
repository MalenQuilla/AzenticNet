package malenquilla.cds.gateway.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {
    @GetMapping("/auth-fallback")
    public String authFallback() {
        return "Auth service is not available";
    }

    @GetMapping("/user-fallback")
    public String userFallback() {
        return "User service is not available";
    }
}
