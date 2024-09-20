package malenquilla.cds.authentication.services;

import jakarta.annotation.PostConstruct;
import malenquilla.cds.authentication.enums.EAuthority;
import malenquilla.cds.authentication.models.AuthorityModel;
import malenquilla.cds.authentication.repositories.AuthorityRepository;
import malenquilla.cds.common.exceptions.BadRequestException;
import malenquilla.cds.common.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class AuthorityService {
    private final static Logger logger = Logger.getLogger(AuthorityService.class.getName());

    private final AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityService(
            AuthorityRepository authorityRepository
    ) {
        this.authorityRepository = authorityRepository;
    }

    @PostConstruct
    private void initDatabase() {
        EAuthority.stream()
                  .forEach((authority) -> {
                      try {
                          this.createAuthority(authority);
                      } catch (BadRequestException badRequestException) {
                          logger.warning("Cannot init authority since: " + badRequestException.getMessage());
                      }
                  });
    }

    public void createAuthority(EAuthority authority) throws BadRequestException {
        if (this.authorityRepository.existsByName(authority))
            throw new BadRequestException("Authority " + authority + " already exist");

        AuthorityModel authorityModel = new AuthorityModel();
        authorityModel.setName(authority);

        this.authorityRepository.save(authorityModel);
    }

    public AuthorityModel getAuthorityByName(String name) {
        return this.authorityRepository.getByName(EAuthority.valueOf(name))
                                       .orElseThrow(NotFoundException::new);
    }
}
