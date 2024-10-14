package malenquilla.cds.authentication.services;

import malenquilla.cds.authentication.dtos.RoleDTO;
import malenquilla.cds.common.payloads.requests.PaginationRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface RoleService {
    void createRole(RoleDTO roleDTO);

    List<RoleDTO> getRoles(PaginationRequest paginationRequest);

    RoleDTO getRoleById(Long id);

    @Transactional
    void updateRole(Long id, RoleDTO roleDTO);

    void deleteRoleById(Long id);
}
