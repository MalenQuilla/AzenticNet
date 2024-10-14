package malenquilla.cds.authentication.controllers.rest;

import lombok.RequiredArgsConstructor;
import malenquilla.cds.authentication.dtos.RoleDTO;
import malenquilla.cds.authentication.services.RoleService;
import malenquilla.cds.common.payloads.requests.PaginationRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@PreAuthorize("hasAuthority('READ_ROLES')")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    public List<RoleDTO> getRoles(PaginationRequest paginationRequest) {
        return this.roleService.getRoles(paginationRequest);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_ROLES')")
    public void createRole(@RequestBody RoleDTO roleDTO) {
        this.roleService.createRole(roleDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE_ROLES')")
    public void updateRole(@PathVariable Long id, @RequestBody RoleDTO roleDTO) {
        this.roleService.updateRole(id, roleDTO);
    }

    @GetMapping("/{id}")
    public RoleDTO getRoleById(@PathVariable Long id) {
        return this.roleService.getRoleById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_ROLES')")
    public void deleteRoleById(@PathVariable Long id) {
        this.roleService.deleteRoleById(id);
    }
}
