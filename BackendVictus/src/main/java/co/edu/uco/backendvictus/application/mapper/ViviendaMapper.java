package co.edu.uco.backendvictus.application.mapper;

import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import co.edu.uco.backendvictus.application.dto.vivienda.ViviendaCreateRequest;
import co.edu.uco.backendvictus.application.dto.vivienda.ViviendaResponse;
import co.edu.uco.backendvictus.crosscutting.helpers.Sanitizer;
import co.edu.uco.backendvictus.domain.model.Vivienda;

@Component
public class ViviendaMapper {

    public Vivienda toDomain(final UUID id, final ViviendaCreateRequest request) {
        final Map<String, Object> sanitizedMetadata = Sanitizer.sanitizeMetadata(request.metadatos());
        return Vivienda.create(id, Sanitizer.sanitize(request.numero()), Sanitizer.sanitize(request.tipo()),
                Sanitizer.sanitize(request.estado()), request.conjuntoId(), sanitizedMetadata);
    }

    public ViviendaResponse toResponse(final Vivienda vivienda) {
        return new ViviendaResponse(vivienda.getId(), vivienda.getNumero(), vivienda.getTipo(), vivienda.getEstado(),
                vivienda.getConjuntoId(), vivienda.getMetadatos());
    }
}
