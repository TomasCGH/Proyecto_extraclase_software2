package co.edu.uco.backendvictus.infrastructure.secondary.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "conjunto_residencial")
public class ConjuntoResidencialJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 180)
    private String direccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ciudad_id", nullable = false)
    private CiudadJpaEntity ciudad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "administrador_id", nullable = false)
    private AdministradorJpaEntity administrador;

    @Column(nullable = false)
    private boolean activo;

    protected ConjuntoResidencialJpaEntity() {
        // JPA
    }

    public ConjuntoResidencialJpaEntity(final UUID id, final String nombre, final String direccion,
            final CiudadJpaEntity ciudad, final AdministradorJpaEntity administrador, final boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.administrador = administrador;
        this.activo = activo;
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(final String direccion) {
        this.direccion = direccion;
    }

    public CiudadJpaEntity getCiudad() {
        return ciudad;
    }

    public void setCiudad(final CiudadJpaEntity ciudad) {
        this.ciudad = ciudad;
    }

    public AdministradorJpaEntity getAdministrador() {
        return administrador;
    }

    public void setAdministrador(final AdministradorJpaEntity administrador) {
        this.administrador = administrador;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(final boolean activo) {
        this.activo = activo;
    }
}
