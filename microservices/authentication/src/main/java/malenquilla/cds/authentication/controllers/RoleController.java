package malenquilla.cds.authentication.controllers;

import malenquilla.cds.authentication.services.RoleService;
import malenquilla.cds.common.payloads.requests.PaginationRequest;
import malenquilla.cds.common.dtos.authentication.RoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@PreAuthorize("hasAuthority('VIEW')")
public class RoleController {
    private final RoleService roleService;

    @Autowired
    public RoleController(
            RoleService roleService
    ) {
        this.roleService = roleService;
    }

    @GetMapping
    public List<RoleDTO> getRoles(PaginationRequest paginationRequest) {
        return this.roleService.getRoles(paginationRequest);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE')")
    public void createRole(RoleDTO roleDTO) {
        this.roleService.createRole(roleDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE')")
    public void updateRole(@PathVariable Long id, RoleDTO roleDTO) {
        this.roleService.updateRole(id, roleDTO);
    }

    @GetMapping("/{id}")
    public RoleDTO getRoleById(@PathVariable Long id) {
        return this.roleService.getRoleById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE')")
    public void deleteRoleById(@PathVariable Long id) {
        this.roleService.deleteRoleById(id);
    }
}
