package co.edu.uco.backendvictus.application.mapper;

import java.util.List;
import java.util.UUID;

import co.edu.uco.backendvictus.application.dto.conjunto.ConjuntoCreateRequest;
import co.edu.uco.backendvictus.application.dto.conjunto.ConjuntoResponse;
import co.edu.uco.backendvictus.application.dto.conjunto.ConjuntoUpdateRequest;
import co.edu.uco.backendvictus.domain.model.Administrador;
import co.edu.uco.backendvictus.domain.model.Ciudad;
import co.edu.uco.backendvictus.domain.model.ConjuntoResidencial;

public final class ConjuntoApplicationMapper {

    private ConjuntoApplicationMapper() {
    }

    public static ConjuntoResidencial toDomain(final UUID id, final ConjuntoCreateRequest request,
            final Ciudad ciudad, final Administrador administrador) {
        return ConjuntoResidencial.create(id, request.nombre(), request.direccion(), ciudad, administrador,
                request.activo());
    }

    public static ConjuntoResidencial toDomain(final ConjuntoUpdateRequest request, final Ciudad ciudad,
            final Administrador administrador) {
        return ConjuntoResidencial.create(request.id(), request.nombre(), request.direccion(), ciudad, administrador,
                request.activo());
    }

    public static ConjuntoResponse toResponse(final ConjuntoResidencial conjuntoResidencial) {
        return new ConjuntoResponse(conjuntoResidencial.getId(), conjuntoResidencial.getCiudad().getId(),
                conjuntoResidencial.getAdministrador().getId(), conjuntoResidencial.getNombre(),
                conjuntoResidencial.getDireccion(), conjuntoResidencial.isActivo());
    }

    public static List<ConjuntoResponse> toResponseList(final List<ConjuntoResidencial> conjuntos) {
        return conjuntos.stream().map(ConjuntoApplicationMapper::toResponse).toList();
    }
}
