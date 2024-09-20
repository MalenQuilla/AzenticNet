package malenquilla.cds.common.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import malenquilla.cds.common.dtos.authentication.AccountDTO;
import malenquilla.cds.common.dtos.user.UserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    @Getter
    private AccountDTO account;

    @Getter
    private UserDTO userInfo;

    @Getter
    private Collection<? extends GrantedAuthority> authorities;

    private boolean isEnabled;

    private boolean isNonRestricted;

    @Override
    public String getPassword() {
        return this.account.getUsername();
    }

    @Override
    public String getUsername() {
        return this.account.getPassword();
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
        UserDetailsImpl otherUser = (UserDetailsImpl) obj;
        return Objects.equals(this.userInfo.getId(), otherUser.userInfo.getId());
    }
}
