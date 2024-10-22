package malenquilla.cds.authentication.services.impl;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import malenquilla.cds.authentication.models.AccountDetails;
import malenquilla.cds.authentication.payloads.requests.LoginRequest;
import malenquilla.cds.authentication.repositories.RoleRepository;
import malenquilla.cds.authentication.services.AuthenticationService;
import malenquilla.cds.authentication.utils.JwtUtils;
import malenquilla.cds.common.exceptions.UnauthorizedException;
import malenquilla.cds.common.utils.CookiesUtils;
import malenquilla.cds.grpc.proto.AuthoritiesDetailsGrpc;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Primary
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtUtils jwtUtils;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final AccountDetailsServiceImpl accountDetailsService;
    private final CookiesUtils cookiesUtils;

    @Override
    public AccountDetails authenticateHeaderInternal(String accessToken) throws UnauthorizedException {
        this.jwtUtils.validateAccessToken(accessToken);

        Long userId = this.jwtUtils.getUserIdFromJwt(accessToken);

        return this.accountDetailsService.loadAccountByUserId(userId);
    }

    @Override
    public AuthoritiesDetailsGrpc authenticateHeaderExternal(String accessToken) throws UnauthorizedException {
        this.jwtUtils.validateAccessToken(accessToken);

        Long userId = this.jwtUtils.getUserIdFromJwt(accessToken);
        Long roleId = this.jwtUtils.getRoleIdFromJwt(accessToken);

        Set<? extends GrantedAuthority> authorities = this.roleRepository.findById(roleId)
                                                                         .orElseThrow(UnauthorizedException::new)
                                                                         .getAuthorities();

        return AuthoritiesDetailsGrpc.newBuilder()
                                     .setUserId(userId)
                                     .addAllAuthorities(
                                             authorities.stream()
                                                        .map(GrantedAuthority::getAuthority)
                                                        .toList()
                                     )
                                     .build();
    }

    @Override
    public void login(LoginRequest request, HttpServletResponse response) throws AuthenticationException {
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getPrinciple(), request.getPassword())
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        String accessToken = jwtUtils.generateAccessToken(authentication);
        String refreshToken = jwtUtils.generateRefreshToken(authentication);

        this.cookiesUtils.addAccessCookie(accessToken, response);
        this.cookiesUtils.addRefreshCookie(refreshToken, response);
    }

    @Override
    public void requestActivate(Long id) {

    }

    @Override
    public void activate(Long id, String activationCode) {

    }

    @Override
    public void refresh(String refresh, HttpServletResponse response) throws UnauthorizedException {
        this.jwtUtils.validateRefreshToken(refresh);

        Long userId = this.jwtUtils.getUserIdFromJwt(refresh);
        AccountDetails accountDetails = this.accountDetailsService.loadAccountByUserId(userId);

        String accessToken = this.jwtUtils.generateAccessToken(accountDetails);
        String refreshToken = this.jwtUtils.generateRefreshToken(accountDetails);

        this.cookiesUtils.addAccessCookie(accessToken, response);
        this.cookiesUtils.addRefreshCookie(refreshToken, response);
    }
}
