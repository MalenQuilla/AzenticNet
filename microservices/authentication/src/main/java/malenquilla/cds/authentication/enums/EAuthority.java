package malenquilla.cds.authentication.enums;

import org.springframework.security.core.GrantedAuthority;

import java.util.stream.Stream;

/**
 * Add authorities here to automatically add to database
 */
public enum EAuthority implements GrantedAuthority {
    VIEW_ROLES,
    MANAGE_ROLES,
    VIEW_ACCOUNTS,
    MANAGE_ACCOUNTS,
    ;

    @Override
    public String getAuthority() {
        return this.name();
    }

    public static Stream<EAuthority> stream() {
        return Stream.of(EAuthority.values());
    }
}
