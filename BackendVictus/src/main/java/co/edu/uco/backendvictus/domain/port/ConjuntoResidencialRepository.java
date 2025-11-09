package co.edu.uco.backendvictus.domain.port;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import co.edu.uco.backendvictus.domain.model.ConjuntoResidencial;

public interface ConjuntoResidencialRepository {

    ConjuntoResidencial save(ConjuntoResidencial conjuntoResidencial);

    Optional<ConjuntoResidencial> findById(UUID id);

    List<ConjuntoResidencial> findAll();

    void deleteById(UUID id);
}
