package co.edu.uco.easy.victusresidencias.victus_api.entity;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.*;
import jakarta.persistence.*;

import java.util.UUID;

@MappedSuperclass
public abstract class DomainEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected UUID id;
    
    protected DomainEntity() {
        this.id = UUIDHelper.getDefault();
    }
    
    protected DomainEntity(final UUID id) {
        setId(id);
    }

    protected UUID getId() {
        return id;
    }
    
    protected void setId(final UUID id) {
        this.id = UUIDHelper.getDefault(id, UUIDHelper.getDefault());
    }

}