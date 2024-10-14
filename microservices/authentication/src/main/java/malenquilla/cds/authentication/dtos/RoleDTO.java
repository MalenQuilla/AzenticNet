package malenquilla.cds.authentication.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class RoleDTO {
    @Min(0)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private Set<String> authorities;
}
