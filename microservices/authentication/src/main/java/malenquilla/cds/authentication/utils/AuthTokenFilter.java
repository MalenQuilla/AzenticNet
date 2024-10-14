package malenquilla.cds.authentication.utils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import malenquilla.cds.authentication.models.AccountDetails;
import malenquilla.cds.authentication.services.AuthenticationService;
import malenquilla.cds.common.utils.CookiesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {
    // TODO: remove field injection
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private CookiesUtils cookiesUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String accessToken = this.cookiesUtils.getAccessToken(request);

            AccountDetails accountDetails = this.authenticationService.authenticateHeaderInternal(accessToken);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    accountDetails,
                    null,
                    accountDetails.getAuthorities()
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
        } catch (Exception ignored) {
        }

        filterChain.doFilter(request, response);
    }
}
