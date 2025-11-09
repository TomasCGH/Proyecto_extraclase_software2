package co.edu.uco.backendvictus.application.dto.vivienda;

import java.util.Map;
import java.util.UUID;

public record ViviendaResponse(UUID id, String numero, String tipo, String estado, UUID conjuntoId,
        Map<String, Object> metadatos) {
}
