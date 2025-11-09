package co.edu.uco.backendvictus.infrastructure.secondary.mapper;

import co.edu.uco.backendvictus.domain.model.Ciudad;
import co.edu.uco.backendvictus.domain.model.Departamento;
import co.edu.uco.backendvictus.infrastructure.secondary.entity.CiudadJpaEntity;
import co.edu.uco.backendvictus.infrastructure.secondary.entity.DepartamentoJpaEntity;

public final class CiudadEntityMapper {

    private CiudadEntityMapper() {
    }

    public static CiudadJpaEntity toEntity(final Ciudad ciudad) {
        final DepartamentoJpaEntity departamento = DepartamentoEntityMapper.toEntity(ciudad.getDepartamento());
        return new CiudadJpaEntity(ciudad.getId(), departamento, ciudad.getNombre(), ciudad.isActivo());
    }

    public static Ciudad toDomain(final CiudadJpaEntity entity) {
        final Departamento departamento = DepartamentoEntityMapper.toDomain(entity.getDepartamento());
        return Ciudad.create(entity.getId(), entity.getNombre(), departamento, entity.isActivo());
    }
}
