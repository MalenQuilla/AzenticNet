package malenquilla.cds.authentication.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import malenquilla.cds.authentication.enums.EAuthority;
import org.springframework.security.core.GrantedAuthority;

@Data
@Entity
@Table(name = "authority", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
@NoArgsConstructor
public class AuthorityModel implements GrantedAuthority {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EAuthority name;

    @Override
    public String getAuthority() {
        return this.name.name();
    }
}
