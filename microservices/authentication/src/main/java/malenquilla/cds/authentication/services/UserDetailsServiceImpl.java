package malenquilla.cds.authentication.services;

import malenquilla.cds.authentication.dto_mappers.AccountMapper;
import malenquilla.cds.authentication.enums.EStatus;
import malenquilla.cds.authentication.models.AccountModel;
import malenquilla.cds.common.clients.UserClient;
import malenquilla.cds.common.security.UserDetailsImpl;
import malenquilla.cds.authentication.repositories.AccountRepository;
import malenquilla.cds.common.dtos.authentication.AccountDTO;
import malenquilla.cds.common.dtos.user.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserClient userClient;

    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;

    @Autowired
    public UserDetailsServiceImpl(
            UserClient userClient,
            AccountRepository accountRepository,
            AccountMapper accountMapper
    ) {
        this.userClient = userClient;
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountModel accountModel = this.accountRepository.getByUsername(username)
                                                          .orElseThrow(() ->
                                                                  new UsernameNotFoundException("Cannot get user details for: " + username));
        AccountDTO account = this.accountMapper.toDto(accountModel);

        UserDTO user = userClient.getUserByUserId(accountModel.getUserId());

        Collection<? extends GrantedAuthority> authorities = accountModel.getRole().getAuthorities();

        EStatus accountStatus = accountModel.getStatus();
        boolean isEnabled = accountStatus == EStatus.STATUS_ACTIVE;
        boolean isNonRestricted = accountStatus != EStatus.STATUS_RESTRICTED;

        return new UserDetailsImpl(account, user, authorities, isEnabled, isNonRestricted);
    }
}
