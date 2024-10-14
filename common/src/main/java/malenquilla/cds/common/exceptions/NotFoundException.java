package malenquilla.cds.common.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends HTTPException {
    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

    public NotFoundException() {
        super(HttpStatus.NOT_FOUND);
    }
}
