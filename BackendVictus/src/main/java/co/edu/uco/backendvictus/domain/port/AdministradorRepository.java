package co.edu.uco.backendvictus.domain.port;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import co.edu.uco.backendvictus.domain.model.Administrador;

public interface AdministradorRepository {

    Administrador save(Administrador administrador);

    Optional<Administrador> findById(UUID id);

    List<Administrador> findAll();

    void deleteById(UUID id);
}
