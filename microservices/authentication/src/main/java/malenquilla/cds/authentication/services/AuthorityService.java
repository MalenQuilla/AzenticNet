package malenquilla.cds.authentication.services;

import malenquilla.cds.authentication.dtos.AuthorityDTO;
import malenquilla.cds.authentication.models.AuthorityModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuthorityService {
    AuthorityModel getByName(String name);

    AuthorityDTO getById(Long id);

    List<AuthorityDTO> getAll();
}
