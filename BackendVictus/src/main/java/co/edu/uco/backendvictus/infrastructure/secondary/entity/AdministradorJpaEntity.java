package co.edu.uco.backendvictus.infrastructure.secondary.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "administrador")
public class AdministradorJpaEntity {

    @Id
    private UUID id;

    @Column(name = "primer_nombre", nullable = false, length = 60)
    private String primerNombre;

    @Column(name = "segundo_nombres", length = 100)
    private String segundoNombres;

    @Column(name = "primer_apellido", nullable = false, length = 60)
    private String primerApellido;

    @Column(name = "segundo_apellido", length = 60)
    private String segundoApellido;

    @Column(nullable = false, length = 120, unique = true)
    private String email;

    @Column(length = 20)
    private String telefono;

    @Column(nullable = false)
    private boolean activo;

    protected AdministradorJpaEntity() {
        // JPA
    }

    public AdministradorJpaEntity(final UUID id, final String primerNombre, final String segundoNombres,
            final String primerApellido, final String segundoApellido, final String email, final String telefono,
            final boolean activo) {
        this.id = id;
        this.primerNombre = primerNombre;
        this.segundoNombres = segundoNombres;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.email = email;
        this.telefono = telefono;
        this.activo = activo;
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(final String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombres() {
        return segundoNombres;
    }

    public void setSegundoNombres(final String segundoNombres) {
        this.segundoNombres = segundoNombres;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(final String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(final String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(final String telefono) {
        this.telefono = telefono;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(final boolean activo) {
        this.activo = activo;
    }
}
