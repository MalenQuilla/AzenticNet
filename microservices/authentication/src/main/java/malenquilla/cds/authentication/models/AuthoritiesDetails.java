package malenquilla.cds.authentication.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@AllArgsConstructor
public class AuthoritiesDetails {
    private Long userId;

    private Collection<? extends GrantedAuthority> authorities;
}
