package co.edu.uco.backendvictus.infrastructure.secondary.mapper;

import co.edu.uco.backendvictus.domain.model.Administrador;
import co.edu.uco.backendvictus.infrastructure.secondary.entity.AdministradorJpaEntity;

public final class AdministradorEntityMapper {

    private AdministradorEntityMapper() {
    }

    public static AdministradorJpaEntity toEntity(final Administrador administrador) {
        return new AdministradorJpaEntity(administrador.getId(), administrador.getPrimerNombre(),
                administrador.getSegundoNombres(), administrador.getPrimerApellido(),
                administrador.getSegundoApellido(), administrador.getEmail(), administrador.getTelefono(),
                administrador.isActivo());
    }

    public static Administrador toDomain(final AdministradorJpaEntity entity) {
        return Administrador.create(entity.getId(), entity.getPrimerNombre(), entity.getSegundoNombres(),
                entity.getPrimerApellido(), entity.getSegundoApellido(), entity.getEmail(), entity.getTelefono(),
                entity.isActivo());
    }
}
