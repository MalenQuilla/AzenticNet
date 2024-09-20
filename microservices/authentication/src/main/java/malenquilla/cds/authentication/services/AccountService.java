package malenquilla.cds.authentication.services;

import malenquilla.cds.authentication.dto_mappers.AccountMapper;
import malenquilla.cds.authentication.enums.EStatus;
import malenquilla.cds.authentication.models.AccountModel;
import malenquilla.cds.authentication.repositories.AccountRepository;
import malenquilla.cds.common.dtos.authentication.AccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountService(
            AccountRepository accountRepository,
            AccountMapper accountMapper,
            PasswordEncoder passwordEncoder
    ) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public void createAccount(AccountDTO accountDTO) {
        AccountModel accountModel = this.accountMapper.toModel(accountDTO);
        accountModel.setPassword(this.passwordEncoder.encode(accountDTO.getPassword()));
        accountModel.setStatus(EStatus.STATUS_INACTIVE);

        this.accountRepository.save(accountModel);
    }

    public void login(AccountDTO accountDTO) {

    }
}
