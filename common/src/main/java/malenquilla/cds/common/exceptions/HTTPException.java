package malenquilla.cds.common.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
public class HTTPException extends RuntimeException {
    private HttpStatusCode statusCode;

    public HTTPException(HttpStatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public HTTPException(HttpStatusCode statusCode) {
        super();
        this.statusCode = statusCode;
    }
}
