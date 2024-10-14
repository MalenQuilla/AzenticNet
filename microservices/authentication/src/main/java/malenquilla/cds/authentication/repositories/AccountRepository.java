package malenquilla.cds.authentication.repositories;

import malenquilla.cds.authentication.enums.EStatus;
import malenquilla.cds.authentication.models.AccountModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountModel, Long> {
    @Query("select account from AccountModel as account where account.username = :principle or account.email = :principle")
    Optional<AccountModel> getByUsernameOrEmail(String principle);

    Optional<AccountModel> getByUserId(Long userId);

    boolean existsByUserId(Long userId);

    boolean existsByUsername(String username);

    // TODO: double check if we can update the whole account model or seperated field
    @Modifying
    @Query("update AccountModel as account set account = :accountModel where account.userId = :userId")
    void updateByUserId(Long userId, AccountModel accountModel);

    @Modifying
    @Query("update AccountModel as account set account.status = :status where account.userId = :userId")
    void updateStatusByUserId(Long userId, EStatus status);
}
