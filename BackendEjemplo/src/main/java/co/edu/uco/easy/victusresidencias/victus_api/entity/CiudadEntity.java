package co.edu.uco.easy.victusresidencias.victus_api.entity;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.*;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "ciudad")
public class CiudadEntity extends DomainEntity {

    @Column(nullable = false, name = "nombre", length = 50)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "departamento_id", nullable = false)
    private DepartamentoEntity departamento;

    public CiudadEntity() {
        super(UUIDHelper.getDefault());
        setNombre(TextHelper.EMPTY);
        setDepartamento(null);
    }

    public static final CiudadEntity create() {
        return new CiudadEntity();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
    }

    public DepartamentoEntity getDepartamento() {
        return departamento;
    }

    public void setDepartamento(DepartamentoEntity departamento) {
        this.departamento = departamento;
    }

    @Override
    public void setId(final UUID id) {
        super.setId(id);
    }

    @Override
    public UUID getId() {
        return super.getId();
    }
}