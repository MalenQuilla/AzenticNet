package malenquilla.cds.common.exception_handler;

import io.grpc.StatusRuntimeException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import malenquilla.cds.common.exceptions.HTTPException;
import malenquilla.cds.common.grpc.servers.GrpcMethodHandler;
import malenquilla.cds.common.payloads.response.ExceptionResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@RestControllerAdvice
public class CommonExceptionHandler {
    private static final Logger logger = Logger.getLogger(CommonExceptionHandler.class.getName());

    private void logException(WebRequest request) {
        logger.warning("Exception from request: " + request.toString());
    }

    @ExceptionHandler(value = StatusRuntimeException.class)
    @Order(value = Ordered.HIGHEST_PRECEDENCE)
    private ResponseEntity<Object> handleGrpcException(StatusRuntimeException exception, WebRequest request) {
        this.logException(request);

        HttpStatusCode statusCode = HttpStatus.INTERNAL_SERVER_ERROR;

        for (HttpStatusCode key : GrpcMethodHandler.EXCEPTION_MAP.keySet()) {
            if (GrpcMethodHandler.EXCEPTION_MAP.get(key).getCode().equals(exception.getStatus().getCode())) {
                statusCode = key;
                break;
            }
        }

        return this.createExceptionResponseEntity(statusCode, exception.getStatus()
                                                                       .getDescription());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @Order(value = Ordered.HIGHEST_PRECEDENCE)
    private ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, WebRequest request) {
        this.logException(request);

        Set<String> errorsMessage = new HashSet<>(exception.getErrorCount());
        errorsMessage.addAll(exception.getBindingResult()
                                      .getAllErrors()
                                      .stream()
                                      .map((error) ->
                                              String.format(
                                                      "field %s %s",
                                                      ((FieldError) error).getField(),
                                                      error.getDefaultMessage()
                                              ))
                                      .toList());

        return this.createExceptionResponseEntity(HttpStatus.BAD_REQUEST, errorsMessage);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    @Order(value = Ordered.HIGHEST_PRECEDENCE)
    private ResponseEntity<Object> handleValidationException(ConstraintViolationException exception, WebRequest request) {
        this.logException(request);

        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        Set<String> violationMessageSet = new HashSet<>(constraintViolations.size());
        violationMessageSet.addAll(
                constraintViolations.stream()
                                    .map((constraintViolation) ->
                                            String.format(
                                                    "%s value '%s' %s",
                                                    constraintViolation.getPropertyPath(),
                                                    constraintViolation.getInvalidValue(),
                                                    constraintViolation.getMessage()
                                            ))
                                    .toList());

        return this.createExceptionResponseEntity(HttpStatus.BAD_REQUEST, violationMessageSet);
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    @Order(value = Ordered.HIGHEST_PRECEDENCE)
    private ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException exception, WebRequest request) {
        this.logException(request);

        return this.createExceptionResponseEntity(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(value = RuntimeException.class)
    private ResponseEntity<Object> handleGlobalException(RuntimeException exception, WebRequest request) {
        this.logException(request);

        boolean isHttpException = exception instanceof HTTPException;

        HttpStatusCode status = isHttpException
                ? ((HTTPException) exception).getStatusCode()
                : HttpStatus.INTERNAL_SERVER_ERROR;

        String message = isHttpException
                ? exception.getMessage()
                : HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();

        return this.createExceptionResponseEntity(status, message);
    }

    private ResponseEntity<Object> createExceptionResponseEntity(HttpStatusCode statusCode, String message) {
        return ResponseEntity.status(statusCode).body(new ExceptionResponse(statusCode.value(), message));
    }

    private ResponseEntity<Object> createExceptionResponseEntity(HttpStatusCode statusCode, Set<String> messages) {
        return ResponseEntity.status(statusCode).body(
                new ExceptionResponse(
                        statusCode.value(),
                        messages.stream()
                                .findFirst()
                                .orElse(null)
                )
        );
    }
}
