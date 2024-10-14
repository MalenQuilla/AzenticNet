package malenquilla.cds.authentication.services.impl;

import lombok.RequiredArgsConstructor;
import malenquilla.cds.authentication.dtos.AuthorityDTO;
import malenquilla.cds.authentication.enums.EAuthority;
import malenquilla.cds.authentication.models.AuthorityModel;
import malenquilla.cds.authentication.repositories.AuthorityRepository;
import malenquilla.cds.authentication.services.AuthorityService;
import malenquilla.cds.common.exceptions.NotFoundException;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {
    private final AuthorityRepository authorityRepository;

    @Override
    public AuthorityModel getByName(String name) {
        return this.authorityRepository.getByName(EAuthority.valueOf(name))
                                       .orElseThrow(NotFoundException::new);
    }

    @Override
    public AuthorityDTO getById(Long id) {
        AuthorityModel authorityModel = this.authorityRepository.findById(id)
                                                                .orElseThrow(NotFoundException::new);
        return new AuthorityDTO(authorityModel.getId(), authorityModel.getAuthority());
    }

    @Override
    public List<AuthorityDTO> getAll() {
        List<AuthorityModel> authorityModels = this.authorityRepository.findAll();

        return authorityModels.stream()
                              .map(authorityModel ->
                                      new AuthorityDTO(authorityModel.getId(), authorityModel.getAuthority())
                              )
                              .toList();
    }
}
