package malenquilla.cds.common.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends HTTPException {
    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

    public BadRequestException() {
        super(HttpStatus.BAD_REQUEST);
    }
}
