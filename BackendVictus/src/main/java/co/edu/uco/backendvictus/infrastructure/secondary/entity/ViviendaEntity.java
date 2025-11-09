package co.edu.uco.backendvictus.infrastructure.secondary.entity;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("vivienda")
public class ViviendaEntity {

    @Id
    private UUID id;
    private String numero;
    private String tipo;
    private String estado;
    @Column("conjunto_id")
    private UUID conjuntoId;
    @Column("metadatos")
    private String metadatos;
    @Column("created_at")
    private OffsetDateTime createdAt;
    @Column("updated_at")
    private OffsetDateTime updatedAt;

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(final String numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(final String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(final String estado) {
        this.estado = estado;
    }

    public UUID getConjuntoId() {
        return conjuntoId;
    }

    public void setConjuntoId(final UUID conjuntoId) {
        this.conjuntoId = conjuntoId;
    }

    public String getMetadatos() {
        return metadatos;
    }

    public void setMetadatos(final String metadatos) {
        this.metadatos = metadatos;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(final OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
