package co.edu.uco.backendvictus.infrastructure.secondary.mapper;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.uco.backendvictus.crosscutting.exception.InfrastructureException;
import co.edu.uco.backendvictus.domain.model.Vivienda;
import co.edu.uco.backendvictus.infrastructure.secondary.entity.ViviendaEntity;

@Component
public class ViviendaEntityMapper {

    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {
    };

    private final ObjectMapper objectMapper;

    public ViviendaEntityMapper(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ViviendaEntity toEntity(final Vivienda vivienda) {
        final ViviendaEntity entity = new ViviendaEntity();
        entity.setId(vivienda.getId());
        entity.setNumero(vivienda.getNumero());
        entity.setTipo(vivienda.getTipo());
        entity.setEstado(vivienda.getEstado());
        entity.setConjuntoId(vivienda.getConjuntoId());
        entity.setMetadatos(writeMetadata(vivienda.getMetadatos()));
        return entity;
    }

    public Vivienda toDomain(final ViviendaEntity entity) {
        return Vivienda.create(entity.getId(), entity.getNumero(), entity.getTipo(), entity.getEstado(),
                entity.getConjuntoId(), readMetadata(entity.getMetadatos()));
    }

    private String writeMetadata(final Map<String, Object> metadatos) {
        if (metadatos == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(metadatos);
        } catch (final JsonProcessingException exception) {
            throw new InfrastructureException("No fue posible serializar los metadatos de la vivienda", exception);
        }
    }

    private Map<String, Object> readMetadata(final String metadatos) {
        if (metadatos == null || metadatos.isBlank()) {
            return null;
        }
        try {
            return objectMapper.readValue(metadatos, MAP_TYPE);
        } catch (final JsonProcessingException exception) {
            throw new InfrastructureException("No fue posible deserializar los metadatos de la vivienda", exception);
        }
    }
}
