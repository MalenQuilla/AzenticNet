package malenquilla.cds.authentication.configs;

import lombok.RequiredArgsConstructor;
import malenquilla.cds.authentication.enums.EAuthority;
import malenquilla.cds.authentication.enums.EStatus;
import malenquilla.cds.authentication.models.AccountModel;
import malenquilla.cds.authentication.models.AuthorityModel;
import malenquilla.cds.authentication.models.RoleModel;
import malenquilla.cds.authentication.repositories.AccountRepository;
import malenquilla.cds.authentication.repositories.AuthorityRepository;
import malenquilla.cds.authentication.repositories.RoleRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@Configuration
@RequiredArgsConstructor
public class DatabaseMigration {
    private final static Logger logger = Logger.getLogger(DatabaseMigration.class.getName());

    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ValuesConfig valuesConfig;

    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent(ContextRefreshedEvent event) {
        event.getApplicationContext().getBean(DatabaseMigration.class).initAll();
    }

    @Transactional
    public void initAll() {
        this.initAuthorities();
        this.initSuperAdmin();
    }

    private void initSuperAdmin() {
        Set<AuthorityModel> authorities = new HashSet<>(this.authorityRepository.findAll());

        RoleModel roleModel = new RoleModel();
        roleModel.setName(this.valuesConfig.getSuperAdminName());
        roleModel.setAuthorities(authorities);

        this.roleRepository.getByName(this.valuesConfig.getSuperAdminName())
                           .ifPresentOrElse(
                                   (role) -> {
                                       role.setAuthorities(authorities);
                                       this.roleRepository.save(role);
                                   },
                                   () -> this.roleRepository.save(roleModel)
                           );

        if (!this.accountRepository.existsByUsername(this.valuesConfig.getSuperAdminName())) {
            AccountModel accountModel = new AccountModel();
            accountModel.setUsername(this.valuesConfig.getSuperAdminName());
            accountModel.setEmail(this.valuesConfig.getSuperAdminEmail());
            accountModel.setPassword(this.passwordEncoder.encode(this.valuesConfig.getSuperAdminPassword()));
            accountModel.setStatus(EStatus.STATUS_ACTIVE);
            accountModel.setRole(roleModel);
            this.accountRepository.save(accountModel);
        }
    }

    private void initAuthorities() {
        Set<AuthorityModel> authorities = new HashSet<>();
        EAuthority.stream()
                  .forEach((authority) -> {
                              if (this.authorityRepository.existsByName(authority))
                                  return;

                              AuthorityModel authorityModel = new AuthorityModel();
                              authorityModel.setName(authority);
                              authorities.add(authorityModel);
                          }
                  );
        this.authorityRepository.saveAll(authorities);
        logger.info("Initialized " + authorities.size() + " authorities");
    }
}
