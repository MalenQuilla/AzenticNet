package malenquilla.cds.authentication.migrations;

import lombok.RequiredArgsConstructor;
import malenquilla.cds.authentication.enums.EAuthority;
import malenquilla.cds.authentication.enums.EStatus;
import malenquilla.cds.authentication.models.AccountModel;
import malenquilla.cds.authentication.models.AuthorityModel;
import malenquilla.cds.authentication.models.RoleModel;
import malenquilla.cds.authentication.repositories.AccountRepository;
import malenquilla.cds.authentication.repositories.AuthorityRepository;
import malenquilla.cds.authentication.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class DatabaseMigration {
    private final static Logger logger = Logger.getLogger(DatabaseMigration.class.getName());
    private static String SUPER_ADMIN_NAME;
    private static String SUPER_ADMIN_EMAIL;
    private static String SUPER_ADMIN_PASSWORD;

    @Value("${cds.app.super.admin.name}")
    public void setSuperAdminName(String name) {
        DatabaseMigration.SUPER_ADMIN_NAME = name;
    }

    @Value("${cds.app.super.admin.email}")
    public void setSuperAdminEmail(String email) {
        DatabaseMigration.SUPER_ADMIN_EMAIL = email;
    }

    @Value("${cds.app.super.admin.password}")
    public void setSuperAdminPassword(String password) {
        DatabaseMigration.SUPER_ADMIN_PASSWORD = password;
    }

    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

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
        roleModel.setName(SUPER_ADMIN_NAME);
        roleModel.setAuthorities(authorities);

        this.roleRepository.getByName(SUPER_ADMIN_NAME)
                           .ifPresentOrElse(
                                   (role) -> {
                                       role.setAuthorities(authorities);
                                       this.roleRepository.save(role);
                                   },
                                   () -> this.roleRepository.save(roleModel)
                           );

        if (!this.accountRepository.existsByUsername(SUPER_ADMIN_NAME)) {
            AccountModel accountModel = new AccountModel();
            accountModel.setUsername(SUPER_ADMIN_NAME);
            accountModel.setEmail(SUPER_ADMIN_EMAIL);
            accountModel.setPassword(this.passwordEncoder.encode(SUPER_ADMIN_PASSWORD));
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
