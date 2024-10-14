package malenquilla.cds.authentication.configs;

import malenquilla.cds.authentication.migrations.DatabaseMigration;
import malenquilla.cds.authentication.repositories.AccountRepository;
import malenquilla.cds.authentication.repositories.AuthorityRepository;
import malenquilla.cds.authentication.repositories.RoleRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class MigrationConfig {
    @Bean
    public DatabaseMigration databaseMigration(
        AuthorityRepository authorityRepository,
        RoleRepository roleRepository,
        AccountRepository accountRepository,
        PasswordEncoder passwordEncoder
    ) {
        return new DatabaseMigration(authorityRepository, roleRepository, accountRepository, passwordEncoder);
    }
}
