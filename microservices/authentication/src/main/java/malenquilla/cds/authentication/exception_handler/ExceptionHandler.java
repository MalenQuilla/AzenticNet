package malenquilla.cds.authentication.exception_handler;

import malenquilla.cds.common.exception_handler.CommonExceptionHandler;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Import(value = {CommonExceptionHandler.class})
public class ExceptionHandler {
}
