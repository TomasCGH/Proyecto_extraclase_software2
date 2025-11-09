package co.edu.uco.easy.victusresidencias.victus_api.repository;

import co.edu.uco.easy.victusresidencias.victus_api.entity.AdministratorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface AdministratorRepository extends JpaRepository<AdministratorEntity, UUID> {
    boolean existsByName(String name);
}