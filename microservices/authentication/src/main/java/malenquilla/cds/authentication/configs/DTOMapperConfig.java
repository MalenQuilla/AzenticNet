package malenquilla.cds.authentication.configs;

import malenquilla.cds.authentication.dto_mappers.AccountMapper;
import malenquilla.cds.authentication.dto_mappers.RoleMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DTOMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public AccountMapper accountMapper(ModelMapper modelMapper) {
        return new AccountMapper(modelMapper);
    }

    @Bean
    public RoleMapper roleMapper(ModelMapper modelMapper) {
        return new RoleMapper(modelMapper);
    }
}
