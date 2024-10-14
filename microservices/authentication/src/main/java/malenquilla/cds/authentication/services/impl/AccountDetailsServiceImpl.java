package malenquilla.cds.authentication.services.impl;

import lombok.RequiredArgsConstructor;
import malenquilla.cds.authentication.enums.EStatus;
import malenquilla.cds.authentication.models.AccountDetails;
import malenquilla.cds.authentication.models.AccountModel;
import malenquilla.cds.authentication.models.RoleModel;
import malenquilla.cds.authentication.repositories.AccountRepository;
import malenquilla.cds.common.exceptions.NotFoundException;
import malenquilla.cds.common.exceptions.UnauthorizedException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountDetailsServiceImpl implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    public AccountDetails loadUserByUsername(String principle) throws UnauthorizedException {
        AccountModel accountModel =
                this.accountRepository.getByUsernameOrEmail(principle)
                                      .orElseThrow(UnauthorizedException::new);
        return this.toAccountDetails(accountModel);
    }

    public AccountDetails loadAccountByUserId(Long userId) throws UnauthorizedException {
        AccountModel accountModel =
                this.accountRepository.findById(userId)
                                      .orElseThrow(UnauthorizedException::new);
        return this.toAccountDetails(accountModel);
    }

    private AccountDetails toAccountDetails(AccountModel account) {
        RoleModel roleModel = account.getRole();
        EStatus accountStatus = account.getStatus();

        return AccountDetails.builder()
                             .userId(account.getUserId())
                             .username(account.getUsername())
                             .password(account.getPassword())
                             .email(account.getEmail())
                             .roleId(roleModel.getId())
                             .authorities(roleModel.getAuthorities())
                             .isEnabled(accountStatus == EStatus.STATUS_ACTIVE)
                             .isNonRestricted(accountStatus != EStatus.STATUS_RESTRICTED)
                             .build();
    }
}
