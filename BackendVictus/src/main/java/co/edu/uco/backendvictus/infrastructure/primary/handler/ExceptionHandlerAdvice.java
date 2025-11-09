package co.edu.uco.backendvictus.infrastructure.primary.handler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import co.edu.uco.backendvictus.crosscutting.exception.ApplicationException;
import co.edu.uco.backendvictus.crosscutting.exception.DomainException;
import co.edu.uco.backendvictus.crosscutting.exception.InfrastructureException;
import co.edu.uco.backendvictus.crosscutting.helpers.LoggerHelper;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    private static final org.slf4j.Logger LOGGER = LoggerHelper.getLogger(ExceptionHandlerAdvice.class);

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(final DomainException exception,
            final HttpServletRequest request) {
        LOGGER.warn("Error de dominio: {}", exception.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, exception, request);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(final ApplicationException exception,
            final HttpServletRequest request) {
        LOGGER.warn("Error de aplicacion: {}", exception.getMessage());
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, exception, request);
    }

    @ExceptionHandler(InfrastructureException.class)
    public ResponseEntity<ErrorResponse> handleInfrastructureException(final InfrastructureException exception,
            final HttpServletRequest request) {
        LOGGER.error("Error de infraestructura", exception);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(final Exception exception,
            final HttpServletRequest request) {
        LOGGER.error("Error inesperado", exception);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception, request);
    }

    private ResponseEntity<ErrorResponse> buildResponse(final HttpStatus status, final Exception exception,
            final HttpServletRequest request) {
        final ErrorResponse response = new ErrorResponse(LocalDateTime.now(), exception.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(status).body(response);
    }
}
