package co.edu.uco.backendvictus.domain.model;

import java.util.UUID;

import co.edu.uco.backendvictus.crosscutting.helpers.ValidationUtils;

/**
 * Country aggregate root.
 */
public final class Pais {

    private final UUID id;
    private final String nombre;
    private final boolean activo;

    private Pais(final UUID id, final String nombre, final boolean activo) {
        this.id = ValidationUtils.validateUUID(id, "Id del pais");
        this.nombre = ValidationUtils.validateRequiredText(nombre, "Nombre del pais", 120);
        this.activo = activo;
    }

    public static Pais create(final UUID id, final String nombre, final boolean activo) {
        return new Pais(id, nombre, activo);
    }

    public Pais update(final String nombre, final boolean activo) {
        return new Pais(this.id, nombre, activo);
    }

    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isActivo() {
        return activo;
    }
}
