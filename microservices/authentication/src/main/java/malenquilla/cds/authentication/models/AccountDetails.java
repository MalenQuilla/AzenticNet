package malenquilla.cds.authentication.models;

import lombok.Builder;
import lombok.Getter;
import malenquilla.cds.authentication.dtos.AccountDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

public class AccountDetails extends AccountDTO implements UserDetails {
    @Getter
    private Collection<? extends GrantedAuthority> authorities;

    private final boolean isEnabled;

    private final boolean isNonRestricted;

    @Builder
    public AccountDetails(
            Long userId,
            String username,
            String email,
            String password,
            Long roleId,
            Collection<? extends GrantedAuthority> authorities,
            boolean isEnabled,
            boolean isNonRestricted
    ) {
        super(userId, username, email, password, roleId);
        this.authorities = authorities;
        this.isEnabled = isEnabled;
        this.isNonRestricted = isNonRestricted;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isNonRestricted;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;
        AccountDetails otherUser = (AccountDetails) obj;
        return Objects.equals(this.getUsername(), otherUser.getUsername());
    }
}
