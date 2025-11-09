package co.edu.uco.backendvictus.infrastructure.primary.handler;

import java.time.LocalDateTime;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.uco.backendvictus.crosscutting.exception.DomainException;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
public class GlobalErrorHandler implements WebExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalErrorHandler.class);

    private final ObjectMapper objectMapper;

    public GlobalErrorHandler(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(final ServerWebExchange exchange, final Throwable ex) {
        final HttpStatus status = resolveStatus(ex);
        final String traceId = exchange.getRequest().getHeaders().getFirst("X-Trace-Id");
        LOGGER.error("Error manejado - status={} traceId={} path={} mensaje={}", status, traceId,
                exchange.getRequest().getPath(), ex.getMessage(), ex);

        final Map<String, Object> body = new java.util.LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", ex.getMessage());
        if (traceId != null) {
            body.put("traceId", traceId);
        }
        body.put("path", exchange.getRequest().getPath().value());

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        return Mono.fromCallable(() -> objectMapper.writeValueAsBytes(body))
                .flatMap(bytes -> exchange.getResponse()
                        .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes))));
    }

    private HttpStatus resolveStatus(final Throwable ex) {
        if (ex instanceof DomainException) {
            return HttpStatus.BAD_REQUEST;
        }
        if (ex instanceof AccessDeniedException) {
            return HttpStatus.FORBIDDEN;
        }
        if (ex instanceof ResponseStatusException responseStatusException) {
            return (HttpStatus) responseStatusException.getStatusCode();
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
