package malenquilla.cds.authentication.services.impl;

import lombok.RequiredArgsConstructor;
import malenquilla.cds.authentication.dtos.AccountDTO;
import malenquilla.cds.authentication.enums.EStatus;
import malenquilla.cds.authentication.models.AccountModel;
import malenquilla.cds.authentication.models.RoleModel;
import malenquilla.cds.authentication.object_mappers.dto_mappers.AccountMapper;
import malenquilla.cds.authentication.repositories.AccountRepository;
import malenquilla.cds.authentication.repositories.RoleRepository;
import malenquilla.cds.authentication.services.AccountService;
import malenquilla.cds.common.exceptions.NotFoundException;
import malenquilla.cds.common.payloads.requests.PaginationRequest;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public void createAccount(AccountDTO accountDTO) {
        AccountModel accountModel = this.accountMapper.toModel(accountDTO);
        accountModel.setPassword(this.passwordEncoder.encode(accountDTO.getPassword()));
        accountModel.setStatus(EStatus.STATUS_INACTIVE);

        RoleModel role = this.roleRepository.findById(accountDTO.getRoleId())
                                            .orElseThrow(() -> new NotFoundException("Role not found"));
        accountModel.setRole(role);

        this.accountRepository.save(accountModel);
    }

    @Override
    public AccountDTO getByUserId(Long userId) {
        AccountModel accountModel = this.accountRepository.getByUserId(userId)
                                                          .orElseThrow(NotFoundException::new);
        return this.accountMapper.toDto(accountModel);
    }

    @Override
    public List<AccountDTO> getAll(PaginationRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        List<AccountModel> accountModels = this.accountRepository.findAll(pageable)
                                                                 .toList();
        return accountModels.stream()
                            .map(this.accountMapper::toDto)
                            .toList();
    }

    @Override
    public void updateAccount(Long userId, AccountDTO accountDTO) {
        AccountModel accountModel = this.accountRepository.getByUserId(userId)
                                                          .orElseThrow(NotFoundException::new);
        accountModel.setUsername(accountDTO.getUsername());
        accountModel.setPassword(this.passwordEncoder.encode(accountDTO.getPassword()));
        accountModel.setEmail(accountDTO.getEmail());

        RoleModel role = this.roleRepository.findById(accountDTO.getRoleId())
                                            .orElseThrow(() -> new NotFoundException("Role not found"));
        accountModel.setRole(role);

        this.accountRepository.updateByUserId(userId, accountModel);
    }

    private void setStatusByUserId(Long userId, EStatus status) {
        if (!this.accountRepository.existsByUserId(userId))
            throw new NotFoundException("User not found");

        this.accountRepository.updateStatusByUserId(userId, status);
    }

    @Override
    public void activateAccount(Long userId) {
        this.setStatusByUserId(userId, EStatus.STATUS_ACTIVE);
    }

    @Override
    public void deactivateAccount(Long userId) {
        this.setStatusByUserId(userId, EStatus.STATUS_INACTIVE);
    }

    @Override
    public void restrictAccount(Long userId) {
        this.setStatusByUserId(userId, EStatus.STATUS_RESTRICTED);
    }
}
