package malenquilla.cds.authentication.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import malenquilla.cds.authentication.enums.ETokenType;
import malenquilla.cds.common.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.logging.Logger;

public class JwtUtils {
    private static final Logger logger = Logger.getLogger(JwtUtils.class.getName());

    @Value("${cds.app.jwtSecret}")
    private String jwtSecret;
    private SecretKey secretKey;

    @Value("${cds.app.accessJwtExpirationMs}")
    private Long accessTokenExp;

    @Value("${cds.app.refreshJwtExpirationMs}")
    private Long refreshTokenExp;

    @PostConstruct
    public void initSecretKey() {
        this.secretKey = Keys.hmacShaKeyFor(this.jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateJWT(Authentication authentication, ETokenType tokenType) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Long expiration;
        if (tokenType == ETokenType.TYPE_ACCESS_TOKEN) expiration = this.accessTokenExp;
        else expiration = this.refreshTokenExp;

        return Jwts.builder()
                   .subject(userDetails.getUsername())
                   .claim("tokenType", tokenType)
                   .issuedAt(new Date())
                   .expiration(new Date(new Date().getTime() + expiration))
                   .signWith(this.secretKey)
                   .compact();
    }

    public Date getIssuedDateFromJwt(String authToken) {
        return this.getPayloadFromJwt(authToken)
                   .getIssuedAt();
    }

    public String getUsernameFromJwt(String authToken) {
        return this.getPayloadFromJwt(authToken)
                   .getSubject();
    }

    public ETokenType getTokenType(String authToken) {
        return this.getPayloadFromJwt(authToken)
                   .get("tokenType", ETokenType.class);
    }

    public Claims getPayloadFromJwt(String authToken) {
        return Jwts.parser()
                   .verifyWith(this.secretKey)
                   .build()
                   .parseSignedClaims(authToken)
                   .getPayload();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.warning("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            logger.warning("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            logger.warning("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            logger.warning("JWT claims string is empty");
        }
        return false;
    }
}
