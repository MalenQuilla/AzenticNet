package malenquilla.cds.authentication.enums;

import org.springframework.security.core.GrantedAuthority;

import java.util.stream.Stream;

/**
 * Add authorities here to automatically add to database
 */
public enum EAuthority implements GrantedAuthority {
    READ_ROLES,
    CREATE_ROLES,
    UPDATE_ROLES,
    DELETE_ROLES,

    READ_ACCOUNTS,
    CREATE_ACCOUNTS,
    UPDATE_ACCOUNTS,
    DELETE_ACCOUNTS,

    READ_USERS,
    CREATE_USERS,
    UPDATE_USERS,
    DELETE_USERS,

    ;

    @Override
    public String getAuthority() {
        return this.name();
    }

    public static Stream<EAuthority> stream() {
        return Stream.of(EAuthority.values());
    }
}
