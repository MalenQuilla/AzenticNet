package malenquilla.cds.authentication.repositories;

import malenquilla.cds.authentication.models.AuthorityModel;
import malenquilla.cds.authentication.models.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<RoleModel, Long> {
    Optional<RoleModel> getByName(String name);

    @Modifying
    @Query("update RoleModel as roles set roles.name = :name, roles.authorities = :authorities where roles.id = :id")
    void updateById(Long id, String name, Set<AuthorityModel> authorities);
}
