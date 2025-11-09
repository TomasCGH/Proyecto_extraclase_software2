package co.edu.uco.easy.victusresidencias.victus_api.repository;

import co.edu.uco.easy.victusresidencias.victus_api.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, UUID> {
    boolean existsByCorreoElectronico(String correoElectronico);
    boolean existsByNumeroIdentificacionAndTipoIdentificacionId(String numeroIdentificacion, UUID tipoIdentificacionId);
    Optional<UsuarioEntity> findByCorreoElectronico(String correoElectronico);
}