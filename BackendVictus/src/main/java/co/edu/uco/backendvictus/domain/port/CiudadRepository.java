package co.edu.uco.backendvictus.domain.port;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import co.edu.uco.backendvictus.domain.model.Ciudad;

public interface CiudadRepository {

    Ciudad save(Ciudad ciudad);

    Optional<Ciudad> findById(UUID id);

    List<Ciudad> findAll();

    void deleteById(UUID id);
}
