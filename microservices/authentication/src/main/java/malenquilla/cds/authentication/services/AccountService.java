package malenquilla.cds.authentication.services;

import malenquilla.cds.authentication.dtos.AccountDTO;
import malenquilla.cds.common.payloads.requests.PaginationRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService {
    void createAccount(AccountDTO accountDTO);

    AccountDTO getByUserId(Long userId);

    List<AccountDTO> getAll(PaginationRequest request);

    void updateAccount(Long userId, AccountDTO accountDTO);

    void activateAccount(Long userId);

    void deactivateAccount(Long userId);

    void restrictAccount(Long userId);
}
