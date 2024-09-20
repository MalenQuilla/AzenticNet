package malenquilla.cds.authentication.services;

import jakarta.transaction.Transactional;
import malenquilla.cds.authentication.dto_mappers.RoleMapper;
import malenquilla.cds.authentication.models.AuthorityModel;
import malenquilla.cds.authentication.models.RoleModel;
import malenquilla.cds.authentication.repositories.RoleRepository;
import malenquilla.cds.common.exceptions.NotFoundException;
import malenquilla.cds.common.payloads.requests.PaginationRequest;
import malenquilla.cds.common.dtos.authentication.RoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(Transactional.TxType.REQUIRED)
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final AuthorityService authorityService;

    @Autowired
    public RoleService(
            RoleRepository roleRepository,
            RoleMapper roleMapper,
            AuthorityService authorityService
    ) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.authorityService = authorityService;
    }

    public void createRole(RoleDTO roleDTO) {
        RoleModel roleModel = this.roleMapper.toModel(roleDTO);

        this.roleRepository.save(roleModel);
    }

    public List<RoleDTO> getRoles(PaginationRequest paginationRequest) {
        Pageable pageable = PageRequest.of(paginationRequest.getPage(), paginationRequest.getSize());

        Page<RoleModel> roles = this.roleRepository.findAll(pageable);

        return roles.stream()
                    .map(this.roleMapper::toDTO)
                    .collect(Collectors.toList());
    }

    public RoleDTO getRoleById(Long id) {
        RoleModel roleModel = this.roleRepository.findById(id)
                                                 .orElseThrow(NotFoundException::new);

        return this.roleMapper.toDTO(roleModel);
    }

    public void updateRole(Long id, RoleDTO roleDTO) {
        Set<String> authoritiesName = roleDTO.getAuthorities();
        Set<AuthorityModel> authorities = authoritiesName.stream()
                                                         .map(this.authorityService::getAuthorityByName)
                                                         .collect(Collectors.toSet());

        this.roleRepository.updateById(id, roleDTO.getName(), authorities);
    }

    public void deleteRoleById(Long id) {
        this.roleRepository.deleteById(id);
    }
}
