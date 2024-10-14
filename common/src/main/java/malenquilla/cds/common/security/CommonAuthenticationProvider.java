package malenquilla.cds.common.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class CommonAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return authentication.isAuthenticated() ? authentication : null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AuthoritiesAuthentication.class.isAssignableFrom(authentication);
    }
}
