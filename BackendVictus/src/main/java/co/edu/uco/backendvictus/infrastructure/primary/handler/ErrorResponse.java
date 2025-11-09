package co.edu.uco.backendvictus.infrastructure.primary.handler;

import java.time.LocalDateTime;

public record ErrorResponse(LocalDateTime timestamp, String message, String path) {
}
