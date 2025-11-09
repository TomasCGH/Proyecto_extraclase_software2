package co.edu.uco.easy.victusresidencias.victus_api.entity;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.*;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "departamento")
public class DepartamentoEntity extends DomainEntity {

    @Column(nullable = false, name = "nombre", length = 50)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "pais_id", nullable = false)
    private PaisEntity pais;

    public DepartamentoEntity() {
        super(UUIDHelper.getDefault());
        setNombre(TextHelper.EMPTY);
        setPais(null);
    }

    public static final DepartamentoEntity create() {
        return new DepartamentoEntity();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
    }

    public PaisEntity getPais() {
        return pais;
    }

    public void setPais(PaisEntity pais) {
        this.pais = pais;
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