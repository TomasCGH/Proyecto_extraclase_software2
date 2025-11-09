package co.edu.uco.easy.victusresidencias.victus_api.repository;

import co.edu.uco.easy.victusresidencias.victus_api.entity.DepartamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface DepartamentoRepository extends JpaRepository<DepartamentoEntity, UUID> {
    boolean existsByNombre(String nombre);
    boolean existsByNombreAndPaisId(String nombre, UUID paisId);
}