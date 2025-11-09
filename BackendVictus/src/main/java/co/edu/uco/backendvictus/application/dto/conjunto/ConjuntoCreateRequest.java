package co.edu.uco.backendvictus.application.dto.conjunto;

import java.util.UUID;

public record ConjuntoCreateRequest(UUID ciudadId, UUID administradorId, String nombre, String direccion,
        boolean activo) {
}
