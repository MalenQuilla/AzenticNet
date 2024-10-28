package malenquilla.cds.authentication.utils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import malenquilla.cds.authentication.configs.ValuesConfig;
import malenquilla.cds.authentication.models.AccountDetails;
import malenquilla.cds.authentication.services.AuthenticationService;
import malenquilla.cds.common.utils.CookiesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Arrays;

public class AuthTokenFilter extends OncePerRequestFilter {
    // TODO: remove field injection
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Autowired
    private CookiesUtils cookiesUtils;

    @Autowired
    private ValuesConfig valuesConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (Arrays.stream(this.valuesConfig.getWhitelist()).noneMatch(request.getRequestURI()::matches)) {
            String accessToken = this.cookiesUtils.getAccessToken(request);

            AccountDetails accountDetails;
            try {
                accountDetails = this.authenticationService.authenticateHeaderInternal(accessToken);
            } catch (Exception e) {
                resolver.resolveException(request, response, null, e);
                return;
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    accountDetails,
                    null,
                    accountDetails.getAuthorities()
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
        }

        filterChain.doFilter(request, response);
    }
}
