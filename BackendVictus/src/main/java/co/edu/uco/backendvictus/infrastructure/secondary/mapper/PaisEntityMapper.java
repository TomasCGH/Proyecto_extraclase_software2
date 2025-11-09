package co.edu.uco.backendvictus.infrastructure.secondary.mapper;

import co.edu.uco.backendvictus.domain.model.Pais;
import co.edu.uco.backendvictus.infrastructure.secondary.entity.PaisJpaEntity;

public final class PaisEntityMapper {

    private PaisEntityMapper() {
    }

    public static PaisJpaEntity toEntity(final Pais pais) {
        return new PaisJpaEntity(pais.getId(), pais.getNombre(), pais.isActivo());
    }

    public static Pais toDomain(final PaisJpaEntity entity) {
        return Pais.create(entity.getId(), entity.getNombre(), entity.isActivo());
    }
}
