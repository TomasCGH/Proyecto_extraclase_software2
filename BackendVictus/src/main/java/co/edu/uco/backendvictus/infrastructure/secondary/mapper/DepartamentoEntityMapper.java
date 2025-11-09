package co.edu.uco.backendvictus.infrastructure.secondary.mapper;

import co.edu.uco.backendvictus.domain.model.Departamento;
import co.edu.uco.backendvictus.domain.model.Pais;
import co.edu.uco.backendvictus.infrastructure.secondary.entity.DepartamentoJpaEntity;
import co.edu.uco.backendvictus.infrastructure.secondary.entity.PaisJpaEntity;

public final class DepartamentoEntityMapper {

    private DepartamentoEntityMapper() {
    }

    public static DepartamentoJpaEntity toEntity(final Departamento departamento) {
        final PaisJpaEntity paisJpaEntity = PaisEntityMapper.toEntity(departamento.getPais());
        return new DepartamentoJpaEntity(departamento.getId(), paisJpaEntity, departamento.getNombre(),
                departamento.isActivo());
    }

    public static Departamento toDomain(final DepartamentoJpaEntity entity) {
        final Pais pais = PaisEntityMapper.toDomain(entity.getPais());
        return Departamento.create(entity.getId(), entity.getNombre(), pais, entity.isActivo());
    }
}
