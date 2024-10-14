package malenquilla.cds.user.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {
    @NotBlank
    private Long id;

    @NotBlank
    private String userId;

    @NotBlank
    private String lastname;

    private String firstname;

    private Date dob;

    private String address;
}
