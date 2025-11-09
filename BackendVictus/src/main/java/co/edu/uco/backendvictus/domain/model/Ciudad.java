package co.edu.uco.backendvictus.domain.model;

import java.util.UUID;

import co.edu.uco.backendvictus.crosscutting.helpers.ValidationUtils;
import co.edu.uco.backendvictus.domain.specification.CiudadTieneDepartamentoSpecification;
import co.edu.uco.backendvictus.domain.specification.SpecificationValidator;

/**
 * City aggregate.
 */
public final class Ciudad {

    private final UUID id;
    private final String nombre;
    private final Departamento departamento;
    private final boolean activo;

    private Ciudad(final UUID id, final String nombre, final Departamento departamento, final boolean activo) {
        this.id = ValidationUtils.validateUUID(id, "Id de la ciudad");
        this.nombre = ValidationUtils.validateRequiredText(nombre, "Nombre de la ciudad", 120);
        this.departamento = departamento;
        this.activo = activo;

        SpecificationValidator.check(CiudadTieneDepartamentoSpecification.INSTANCE, this,
                "La ciudad debe pertenecer a un departamento valido");
    }

    public static Ciudad create(final UUID id, final String nombre, final Departamento departamento,
            final boolean activo) {
        return new Ciudad(id, nombre, departamento, activo);
    }

    public Ciudad update(final String nombre, final Departamento departamento, final boolean activo) {
        return new Ciudad(this.id, nombre, departamento, activo);
    }

    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public boolean isActivo() {
        return activo;
    }
}
