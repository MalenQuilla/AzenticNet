package malenquilla.cds.authentication.payloads.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank
    private String principle;

    @NotBlank
    private String password;
}
