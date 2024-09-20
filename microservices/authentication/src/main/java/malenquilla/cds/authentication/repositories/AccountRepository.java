package malenquilla.cds.authentication.repositories;

import malenquilla.cds.authentication.models.AccountModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountModel, Long> {
    Optional<AccountModel> getByUsername(String name);
}
