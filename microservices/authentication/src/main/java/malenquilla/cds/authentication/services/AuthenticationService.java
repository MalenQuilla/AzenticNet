package malenquilla.cds.authentication.services;

import malenquilla.cds.authentication.enums.ETokenType;
import malenquilla.cds.authentication.utils.JwtUtils;
import malenquilla.cds.common.exceptions.UnauthorizedException;
import malenquilla.cds.common.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final JwtUtils jwtUtils;

    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public AuthenticationService(
            JwtUtils jwtUtils,
            UserDetailsServiceImpl userDetailsService
    ) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    public UsernamePasswordAuthenticationToken validateAuthentication(String authHeader) throws UnauthorizedException {
        String jwt = parseJwt(authHeader);
        if (jwt == null || !jwtUtils.validateJwtToken(jwt) || jwtUtils.getTokenType(jwt) == ETokenType.TYPE_REFRESH_TOKEN)
            throw new UnauthorizedException();

        String username = jwtUtils.getUsernameFromJwt(jwt);

        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getAccount(),
                userDetails.getAuthorities()
        );
    }

    private String parseJwt(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
