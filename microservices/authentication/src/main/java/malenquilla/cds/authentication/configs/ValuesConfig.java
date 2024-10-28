package malenquilla.cds.authentication.configs;

import lombok.Getter;
import malenquilla.cds.common.configs.CommonValuesConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Getter
@Configuration
@Import(CommonValuesConfig.class)
public class ValuesConfig {
    private final String jwtSecret;
    private final Long accessTokenExpirationMs;
    private final Long refreshTokenExpirationMs;

    private final String superAdminName;
    private final String superAdminEmail;
    private final String superAdminPassword;

    private final String[] whitelist;

    public ValuesConfig(
            @Value("${cds.app.jwtSecret}") String jwtSecret,
            @Value("${cds.app.accessJwtExpirationMs}") Long accessExpMs,
            @Value("${cds.app.refreshJwtExpirationMs}") Long refreshExpMs,

            @Value("${cds.app.super.admin.name}") String superAdminName,
            @Value("${cds.app.super.admin.email}") String superAdminEmail,
            @Value("${cds.app.super.admin.password}") String superAdminPassword,

            @Value("${cds.whitelist.uris}") String whiteList
    ) {
        this.jwtSecret = jwtSecret;
        this.accessTokenExpirationMs = accessExpMs;
        this.refreshTokenExpirationMs = refreshExpMs;
        this.superAdminName = superAdminName;
        this.superAdminEmail = superAdminEmail;
        this.superAdminPassword = superAdminPassword;
        this.whitelist = whiteList.split(",");
    }
}
