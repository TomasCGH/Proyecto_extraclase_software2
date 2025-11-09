package co.edu.uco.backendvictus.domain.model;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import co.edu.uco.backendvictus.crosscutting.exception.DomainException;

/**
 * Aggregate root that represents una vivienda dentro de un conjunto residencial.
 */
public final class Vivienda {

    private final UUID id;
    private final String numero;
    private final String tipo;
    private final String estado;

    private static final Set<String> ESTADOS_VALIDOS = Set.of("OCUPADA", "NO_OCUPADA");
    private final UUID conjuntoId;
    private final Map<String, Object> metadatos;

    private Vivienda(final UUID id, final String numero, final String tipo, final String estado,
            final UUID conjuntoId, final Map<String, Object> metadatos) {
        this.id = id;
        this.numero = numero;
        this.tipo = tipo;
        this.estado = estado;
        this.conjuntoId = conjuntoId;
        this.metadatos = Optional.ofNullable(metadatos)
                .map(Collections::unmodifiableMap)
                .orElse(null);
    }

    public static Vivienda create(final UUID id, final String numero, final String tipo, final String estado,
            final UUID conjuntoId, final Map<String, Object> metadatos) {
        final UUID validatedId = Objects.requireNonNull(id, "El identificador de la vivienda es obligatorio");
        final UUID validatedConjuntoId = Objects.requireNonNull(conjuntoId, "El identificador del conjunto es obligatorio");
        final String validatedNumero = requireText(numero, "El numero de la vivienda es obligatorio");
        final String validatedTipo = requireText(tipo, "El tipo de la vivienda es obligatorio");
        final String validatedEstado = validateEstado(estado);
        final String validatedEstado = requireText(estado, "El estado de la vivienda es obligatorio");
        return new Vivienda(validatedId, validatedNumero, validatedTipo, validatedEstado, validatedConjuntoId,
                metadatos);
    }

    private static String validateEstado(final String estado) {
        final String normalizedEstado = requireText(estado, "El estado de la vivienda es obligatorio")
                .toUpperCase(Locale.ROOT);
        if (!ESTADOS_VALIDOS.contains(normalizedEstado)) {
            throw new DomainException("El estado de la vivienda debe ser OCUPADA o NO_OCUPADA");
        }
        return normalizedEstado;
    }

    private static String requireText(final String value, final String message) {
        if (value == null || value.isBlank()) {
            throw new DomainException(message);
        }
        return value;
    }

    public UUID getId() {
        return id;
    }

    public String getNumero() {
        return numero;
    }

    public String getTipo() {
        return tipo;
    }

    public String getEstado() {
        return estado;
    }

    public UUID getConjuntoId() {
        return conjuntoId;
    }

    public Map<String, Object> getMetadatos() {
        return metadatos;
    }
}
