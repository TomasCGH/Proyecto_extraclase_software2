package co.edu.uco.easy.victusresidencias.victus_api.entity;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.*;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "tipo_identificacion")
public class TipoIdentificacionEntity extends DomainEntity {

    @Column(nullable = false, name = "nombre", length = 50)
    private String nombre;

    @Column(nullable = false, name = "codigo", length = 10, unique = true)
    private String codigo;

    public TipoIdentificacionEntity() {
        super(UUIDHelper.getDefault());
        setNombre(TextHelper.EMPTY);
        setCodigo(TextHelper.EMPTY);
    }

    public static final TipoIdentificacionEntity create() {
        return new TipoIdentificacionEntity();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = TextHelper.applyTrim(nombre);
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = TextHelper.applyTrim(codigo);
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