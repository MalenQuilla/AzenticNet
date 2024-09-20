package malenquilla.cds.authentication.repositories;

import malenquilla.cds.authentication.enums.EAuthority;
import malenquilla.cds.authentication.models.AuthorityModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends JpaRepository<AuthorityModel, Long> {
    Boolean existsByName(EAuthority name);

    Optional<AuthorityModel> getByName(EAuthority name);
}
