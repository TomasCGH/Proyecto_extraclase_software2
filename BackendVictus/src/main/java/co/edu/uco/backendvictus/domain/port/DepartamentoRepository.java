package co.edu.uco.backendvictus.domain.port;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import co.edu.uco.backendvictus.domain.model.Departamento;

public interface DepartamentoRepository {

    Departamento save(Departamento departamento);

    Optional<Departamento> findById(UUID id);

    List<Departamento> findAll();

    void deleteById(UUID id);
}
