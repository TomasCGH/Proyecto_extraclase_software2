package co.edu.uco.backendvictus.infrastructure.secondary.mapper;

import co.edu.uco.backendvictus.domain.model.Administrador;
import co.edu.uco.backendvictus.domain.model.Ciudad;
import co.edu.uco.backendvictus.domain.model.ConjuntoResidencial;
import co.edu.uco.backendvictus.infrastructure.secondary.entity.AdministradorJpaEntity;
import co.edu.uco.backendvictus.infrastructure.secondary.entity.CiudadJpaEntity;
import co.edu.uco.backendvictus.infrastructure.secondary.entity.ConjuntoResidencialJpaEntity;

public final class ConjuntoResidencialEntityMapper {

    private ConjuntoResidencialEntityMapper() {
    }

    public static ConjuntoResidencialJpaEntity toEntity(final ConjuntoResidencial conjuntoResidencial,
            final CiudadJpaEntity ciudad, final AdministradorJpaEntity administrador) {
        return new ConjuntoResidencialJpaEntity(conjuntoResidencial.getId(), conjuntoResidencial.getNombre(),
                conjuntoResidencial.getDireccion(), ciudad, administrador, conjuntoResidencial.isActivo());
    }

    public static ConjuntoResidencial toDomain(final ConjuntoResidencialJpaEntity entity) {
        final Ciudad ciudad = CiudadEntityMapper.toDomain(entity.getCiudad());
        final Administrador administrador = AdministradorEntityMapper.toDomain(entity.getAdministrador());
        return ConjuntoResidencial.create(entity.getId(), entity.getNombre(), entity.getDireccion(), ciudad,
                administrador, entity.isActivo());
    }
}
