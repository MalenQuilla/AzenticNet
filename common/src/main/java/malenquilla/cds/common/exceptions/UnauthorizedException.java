package malenquilla.cds.common.exceptions;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends HTTPException {
    public UnauthorizedException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }

    public UnauthorizedException() {
        super(HttpStatus.UNAUTHORIZED);
    }
}
