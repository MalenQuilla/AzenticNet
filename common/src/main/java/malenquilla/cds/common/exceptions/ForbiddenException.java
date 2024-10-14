package malenquilla.cds.common.exceptions;

import org.springframework.http.HttpStatus;


public class ForbiddenException extends HTTPException {
    public ForbiddenException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }

    public ForbiddenException() {
        super(HttpStatus.FORBIDDEN);
    }
}
