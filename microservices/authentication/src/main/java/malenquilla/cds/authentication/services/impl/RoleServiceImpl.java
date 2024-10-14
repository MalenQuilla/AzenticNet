package malenquilla.cds.authentication.services.impl;

import lombok.RequiredArgsConstructor;
import malenquilla.cds.authentication.dtos.RoleDTO;
import malenquilla.cds.authentication.models.AuthorityModel;
import malenquilla.cds.authentication.models.RoleModel;
import malenquilla.cds.authentication.object_mappers.dto_mappers.RoleMapper;
import malenquilla.cds.authentication.repositories.RoleRepository;
import malenquilla.cds.authentication.services.AuthorityService;
import malenquilla.cds.authentication.services.RoleService;
import malenquilla.cds.common.exceptions.NotFoundException;
import malenquilla.cds.common.payloads.requests.PaginationRequest;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Primary
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final AuthorityService authorityService;

    @Override
    public void createRole(RoleDTO roleDTO) {
        RoleModel roleModel = this.roleMapper.toModel(roleDTO);

        this.roleRepository.save(roleModel);
    }

    @Override
    public List<RoleDTO> getRoles(PaginationRequest paginationRequest) {
        Pageable pageable = PageRequest.of(paginationRequest.getPage(), paginationRequest.getSize());

        Page<RoleModel> roles = this.roleRepository.findAll(pageable);

        return roles.stream()
                    .map(this.roleMapper::toDTO)
                    .collect(Collectors.toList());
    }

    @Override
    public RoleDTO getRoleById(Long id) {
        RoleModel roleModel = this.roleRepository.findById(id)
                                                 .orElseThrow(NotFoundException::new);

        return this.roleMapper.toDTO(roleModel);
    }

    @Override
    public void updateRole(Long id, RoleDTO roleDTO) {
        Set<String> authoritiesName = roleDTO.getAuthorities();
        Set<AuthorityModel> authorities = authoritiesName.stream()
                                                         .map(this.authorityService::getByName)
                                                         .collect(Collectors.toSet());

        this.roleRepository.findById(id)
                           .ifPresentOrElse(
                                   (role) -> {
                                       role.setName(roleDTO.getName());
                                       role.setAuthorities(authorities);
                                       this.roleRepository.save(role);
                                   },
                                   () -> {
                                       throw new NotFoundException();
                                   }
                           );
    }

    @Override
    public void deleteRoleById(Long id) {
        this.roleRepository.deleteById(id);
    }
}
