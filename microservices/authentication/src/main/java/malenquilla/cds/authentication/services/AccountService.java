package malenquilla.cds.authentication.services;

import malenquilla.cds.authentication.dtos.AccountDTO;
import malenquilla.cds.common.payloads.requests.PaginationRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService {
    void create(AccountDTO accountDTO);

    AccountDTO getByUserId(Long userId);

    List<AccountDTO> getAll(PaginationRequest request);

    void updateByUserId(Long userId, AccountDTO accountDTO);

    void activate(Long userId);

    void deactivate(Long userId);

    void restrict(Long userId);
}
