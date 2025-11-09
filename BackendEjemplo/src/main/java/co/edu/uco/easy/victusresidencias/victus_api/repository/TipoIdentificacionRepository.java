package co.edu.uco.easy.victusresidencias.victus_api.repository;

import co.edu.uco.easy.victusresidencias.victus_api.entity.TipoIdentificacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface TipoIdentificacionRepository extends JpaRepository<TipoIdentificacionEntity, UUID> {
    boolean existsByNombre(String nombre);
}