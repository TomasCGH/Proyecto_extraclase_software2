package co.edu.uco.backendvictus.application.mapper;

import java.util.List;
import java.util.UUID;

import co.edu.uco.backendvictus.application.dto.pais.PaisCreateRequest;
import co.edu.uco.backendvictus.application.dto.pais.PaisResponse;
import co.edu.uco.backendvictus.application.dto.pais.PaisUpdateRequest;
import co.edu.uco.backendvictus.domain.model.Pais;

public final class PaisApplicationMapper {

    private PaisApplicationMapper() {
    }

    public static Pais toDomain(final UUID id, final PaisCreateRequest request) {
        return Pais.create(id, request.nombre(), request.activo());
    }

    public static Pais toDomain(final PaisUpdateRequest request) {
        return Pais.create(request.id(), request.nombre(), request.activo());
    }

    public static PaisResponse toResponse(final Pais pais) {
        return new PaisResponse(pais.getId(), pais.getNombre(), pais.isActivo());
    }

    public static List<PaisResponse> toResponseList(final List<Pais> paises) {
        return paises.stream().map(PaisApplicationMapper::toResponse).toList();
    }
}
