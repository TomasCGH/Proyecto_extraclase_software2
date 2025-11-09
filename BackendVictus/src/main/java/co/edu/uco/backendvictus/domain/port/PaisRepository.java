package co.edu.uco.backendvictus.domain.port;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import co.edu.uco.backendvictus.domain.model.Pais;

public interface PaisRepository {

    Pais save(Pais pais);

    Optional<Pais> findById(UUID id);

    List<Pais> findAll();

    void deleteById(UUID id);
}
