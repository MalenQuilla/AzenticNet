package malenquilla.cds.common.exceptions;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
public class NotFoundException extends HTTPException {
    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
