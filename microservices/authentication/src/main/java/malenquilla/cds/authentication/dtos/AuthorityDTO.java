package malenquilla.cds.authentication.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuthorityDTO {
    @NotBlank
    private final Long id;

    @NotBlank
    private final String name;
}
