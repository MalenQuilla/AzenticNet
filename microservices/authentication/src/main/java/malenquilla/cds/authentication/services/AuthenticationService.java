package malenquilla.cds.authentication.services;

import jakarta.servlet.http.HttpServletResponse;
import malenquilla.cds.authentication.models.AccountDetails;
import malenquilla.cds.authentication.payloads.requests.LoginRequest;
import malenquilla.cds.common.exceptions.UnauthorizedException;
import malenquilla.cds.grpc.proto.AuthoritiesDetailsGrpc;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {
    AccountDetails authenticateHeaderInternal(String accessToken) throws UnauthorizedException;

    AuthoritiesDetailsGrpc authenticateHeaderExternal(String accessToken) throws UnauthorizedException;

    void login(LoginRequest request, HttpServletResponse response) throws AuthenticationException;

    void requestActivate(Long id);

    void activate(Long id, String activationCode);

    void refresh(String refreshToken, HttpServletResponse response) throws UnauthorizedException;
}
