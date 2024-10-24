package malenquilla.cds.authentication.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import malenquilla.cds.authentication.enums.ETokenType;
import malenquilla.cds.authentication.models.AccountDetails;
import malenquilla.cds.common.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.logging.Logger;

public class JwtUtils {
    private static final Logger logger = Logger.getLogger(JwtUtils.class.getName());
    private static String JWT_SECRET;
    private static Long ACCESS_TOKEN_EXPIRATION_MS;
    private static Long REFRESH_TOKEN_EXPIRATION_MS;

    private SecretKey secretKey;

    @Value("${cds.app.jwtSecret}")
    public void setJwtSecret(String jwtSecret) {
        JwtUtils.JWT_SECRET = jwtSecret;
    }

    @Value("${cds.app.accessJwtExpirationMs}")
    public void setAccessTokenExpirationMs(Long accessTokenExpirationMs) {
        JwtUtils.ACCESS_TOKEN_EXPIRATION_MS = accessTokenExpirationMs;
    }

    @Value("${cds.app.refreshJwtExpirationMs}")
    public void setRefreshTokenExpirationMs(Long refreshTokenExpirationMs) {
        JwtUtils.REFRESH_TOKEN_EXPIRATION_MS = refreshTokenExpirationMs;
    }

    @PostConstruct
    public void initSecretKey() {
        this.secretKey = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public String generateJWT(Authentication authentication, ETokenType tokenType) {
        AccountDetails accountDetails = (AccountDetails) authentication.getPrincipal();

        return this.generateJWT(accountDetails, tokenType);
    }

    public String generateJWT(AccountDetails accountDetails, ETokenType tokenType) {
        Long expiration;
        if (tokenType == ETokenType.TYPE_ACCESS_TOKEN) expiration = ACCESS_TOKEN_EXPIRATION_MS;
        else expiration = REFRESH_TOKEN_EXPIRATION_MS;

        return Jwts.builder()
                   .claim("tokenType", tokenType)
                   .claim("uid", accountDetails.getUserId())
                   .claim("rid", accountDetails.getRoleId())
                   .issuedAt(new Date())
                   .expiration(new Date(new Date().getTime() + expiration))
                   .signWith(this.secretKey)
                   .compact();
    }

    public String generateAccessToken(Authentication authentication) {
        return this.generateJWT(authentication, ETokenType.TYPE_ACCESS_TOKEN);
    }

    public String generateAccessToken(AccountDetails accountDetails) {
        return this.generateJWT(accountDetails, ETokenType.TYPE_ACCESS_TOKEN);
    }

    public String generateRefreshToken(Authentication authentication) {
        return this.generateJWT(authentication, ETokenType.TYPE_REFRESH_TOKEN);
    }

    public String generateRefreshToken(AccountDetails accountDetails) {
        return this.generateJWT(accountDetails, ETokenType.TYPE_REFRESH_TOKEN);
    }

    public Date getIssuedDateFromJwt(String authToken) {
        return this.getPayloadFromJwt(authToken)
                   .getIssuedAt();
    }

    public Long getUserIdFromJwt(String authToken) {
        return this.getPayloadFromJwt(authToken)
                   .get("uid", Long.class);
    }

    public Long getRoleIdFromJwt(String authToken) {
        return this.getPayloadFromJwt(authToken)
                   .get("rid", Long.class);
    }

    public ETokenType getTokenType(String authToken) {
        String name = this.getPayloadFromJwt(authToken)
                          .get("tokenType", String.class);
        return ETokenType.valueOf(name);
    }

    public Claims getPayloadFromJwt(String authToken) {
        return Jwts.parser()
                   .verifyWith(this.secretKey)
                   .build()
                   .parseSignedClaims(authToken)
                   .getPayload();
    }

    public boolean isInvalidJwt(String authToken) {
        try {
            Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(authToken);
            return false;
        } catch (Exception e) {
            logger.warning("Invalid JWT token");
        }
        return true;
    }

    public void validateAccessToken(String access) {
        if (access == null || access.isEmpty() || access.isBlank())
            throw new UnauthorizedException();

        if (this.isInvalidJwt(access) || this.getTokenType(access) != ETokenType.TYPE_ACCESS_TOKEN)
            throw new UnauthorizedException("Access Denied");
    }

    public void validateRefreshToken(String refresh) {
        if (this.isInvalidJwt(refresh) || this.getTokenType(refresh) != ETokenType.TYPE_REFRESH_TOKEN)
            throw new UnauthorizedException();
    }
}
