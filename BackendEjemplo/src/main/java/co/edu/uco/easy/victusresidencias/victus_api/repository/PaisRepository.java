package co.edu.uco.easy.victusresidencias.victus_api.repository;

import co.edu.uco.easy.victusresidencias.victus_api.entity.PaisEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface PaisRepository extends JpaRepository<PaisEntity, UUID> {
    boolean existsByNombre(String nombre);
}