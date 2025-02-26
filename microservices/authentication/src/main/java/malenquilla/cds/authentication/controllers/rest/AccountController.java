package malenquilla.cds.authentication.controllers.rest;

import lombok.RequiredArgsConstructor;
import malenquilla.cds.authentication.dtos.AccountDTO;
import malenquilla.cds.authentication.services.AccountService;
import malenquilla.cds.common.payloads.requests.PaginationRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_ACCOUNTS')")
    public void create(@RequestBody AccountDTO accountDTO) {
        this.accountService.create(accountDTO);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('READ_ACCOUNTS')")
    public List<AccountDTO> getAll(@RequestBody PaginationRequest request) {
        return this.accountService.getAll(request);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('READ_ACCOUNTS') || authentication.principal.equals(userId)")
    public AccountDTO getByUserId(@PathVariable Long userId) {
        return this.accountService.getByUserId(userId);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('UPDATE_ACCOUNTS') || authentication.principal.equals(userId)")
    public void updateByUserId(@PathVariable Long userId, @RequestBody AccountDTO accountDTO) {
        this.accountService.updateByUserId(userId, accountDTO);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('DELETE_ACCOUNTS')")
    public void restrict(@PathVariable Long userId) {
        this.accountService.restrict(userId);
    }

    @PostMapping("/{userId}")
    @PreAuthorize("hasAuthority('UPDATE_ACCOUNTS')")
    public void activate(@PathVariable Long userId) {
        this.accountService.activate(userId);
    }

    @PatchMapping("/{userId}")
    @PreAuthorize("hasAuthority('UPDATE_ACCOUNTS') || authentication.principal.equals(userId)")
    public void deactivate(@PathVariable Long userId) {
        this.accountService.deactivate(userId);
    }
}
