package co.edu.uco.backendvictus.infrastructure.secondary.mapper;

import org.springframework.stereotype.Component;

import co.edu.uco.backendvictus.domain.model.Vivienda;
import co.edu.uco.backendvictus.infrastructure.secondary.entity.ViviendaEntity;

@Component
public class ViviendaEntityMapper {

    public ViviendaEntity toEntity(final Vivienda vivienda) {
        final ViviendaEntity entity = new ViviendaEntity();
        entity.setId(vivienda.getId());
        entity.setNumero(vivienda.getNumero());
        entity.setTipo(vivienda.getTipo());
        entity.setEstado(vivienda.getEstado());
        entity.setConjuntoId(vivienda.getConjuntoId());
        entity.setMetadatos(vivienda.getMetadatos());
        return entity;
    }

    public Vivienda toDomain(final ViviendaEntity entity) {
        return Vivienda.create(entity.getId(), entity.getNumero(), entity.getTipo(), entity.getEstado(),
                entity.getConjuntoId(), entity.getMetadatos());
    }
}
