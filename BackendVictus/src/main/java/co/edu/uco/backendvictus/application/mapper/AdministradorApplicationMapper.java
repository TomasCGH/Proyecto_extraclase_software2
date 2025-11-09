package co.edu.uco.backendvictus.application.mapper;

import java.util.List;
import java.util.UUID;

import co.edu.uco.backendvictus.application.dto.administrador.AdministradorCreateRequest;
import co.edu.uco.backendvictus.application.dto.administrador.AdministradorResponse;
import co.edu.uco.backendvictus.application.dto.administrador.AdministradorUpdateRequest;
import co.edu.uco.backendvictus.domain.model.Administrador;

public final class AdministradorApplicationMapper {

    private AdministradorApplicationMapper() {
    }

    public static Administrador toDomain(final UUID id, final AdministradorCreateRequest request) {
        return Administrador.create(id, request.primerNombre(), request.segundoNombres(), request.primerApellido(),
                request.segundoApellido(), request.email(), request.telefono(), request.activo());
    }

    public static Administrador toDomain(final AdministradorUpdateRequest request) {
        return Administrador.create(request.id(), request.primerNombre(), request.segundoNombres(),
                request.primerApellido(), request.segundoApellido(), request.email(), request.telefono(),
                request.activo());
    }

    public static AdministradorResponse toResponse(final Administrador administrador) {
        return new AdministradorResponse(administrador.getId(), administrador.getPrimerNombre(),
                administrador.getSegundoNombres(), administrador.getPrimerApellido(),
                administrador.getSegundoApellido(), administrador.getEmail(), administrador.getTelefono(),
                administrador.isActivo());
    }

    public static List<AdministradorResponse> toResponseList(final List<Administrador> administradores) {
        return administradores.stream().map(AdministradorApplicationMapper::toResponse).toList();
    }
}
