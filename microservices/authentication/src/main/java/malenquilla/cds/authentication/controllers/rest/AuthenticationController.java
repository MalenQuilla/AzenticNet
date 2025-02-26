package malenquilla.cds.authentication.controllers.rest;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import malenquilla.cds.authentication.dtos.AccountDTO;
import malenquilla.cds.authentication.payloads.requests.LoginRequest;
import malenquilla.cds.authentication.services.AccountService;
import malenquilla.cds.authentication.services.AuthenticationService;
import malenquilla.cds.common.enums.ECookies;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final AccountService accountService;

    @PostMapping("/login")
    public void login(@Validated @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        this.authenticationService.login(loginRequest, response);
    }

    @PostMapping("/register")
    public void register(@Validated @RequestBody AccountDTO accountDTO) {
        this.accountService.create(accountDTO);
    }

    @GetMapping("/request-activation/{id}")
    public void requestActivate(@PathVariable Long id) {
        this.authenticationService.requestActivate(id);
    }

    @PostMapping("/activate/{id}/{activationCode}")
    public void activate(@PathVariable Long id, @PathVariable String activationCode) {
        this.authenticationService.activate(id, activationCode);
    }

    @PostMapping("/refresh")
    public void refresh(
            @CookieValue(value = ECookies.REFRESH_TOKEN, required = false) String cookie,
            HttpServletResponse response) {
        this.authenticationService.refresh(cookie, response);
    }
}
