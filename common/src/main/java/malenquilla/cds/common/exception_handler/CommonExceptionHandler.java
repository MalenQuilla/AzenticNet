package malenquilla.cds.common.exception_handler;

import malenquilla.cds.common.exceptions.HTTPException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.logging.Logger;

@RestControllerAdvice
public class CommonExceptionHandler extends ResponseEntityExceptionHandler {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @ExceptionHandler(value = {RuntimeException.class})
    protected ResponseEntity<Object> handleGlobalException(RuntimeException exception, WebRequest request) {
        this.logger.warning(exception.toString() + "\nFrom request: " + request.toString());

        HttpStatusCode status = exception instanceof HTTPException
                ? ((HTTPException) exception).getStatusCode()
                : HttpStatus.INTERNAL_SERVER_ERROR;

        String message = !exception.getMessage().isEmpty()
                ? exception.getMessage()
                : HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();

        return this.handleExceptionInternal(exception, message, new HttpHeaders(), status, request);
    }
}
