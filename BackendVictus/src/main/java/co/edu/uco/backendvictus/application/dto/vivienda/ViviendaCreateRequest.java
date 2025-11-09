package co.edu.uco.backendvictus.application.dto.vivienda;

import java.util.Map;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ViviendaCreateRequest(
        @NotBlank @Pattern(regexp = "^[A-Za-z0-9\\-\\s]{1,10}$") String numero,
        @NotBlank String tipo,
        @NotBlank String estado,
        @NotNull UUID conjuntoId,
        Map<String, Object> metadatos) {
}
