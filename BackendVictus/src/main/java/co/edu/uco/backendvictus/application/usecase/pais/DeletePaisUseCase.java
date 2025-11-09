package co.edu.uco.backendvictus.application.usecase.pais;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.backendvictus.crosscutting.exception.ApplicationException;
import co.edu.uco.backendvictus.domain.port.PaisRepository;

@Service
public class DeletePaisUseCase {

    private final PaisRepository repository;

    public DeletePaisUseCase(final PaisRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void execute(final UUID id) {
        repository.findById(id).orElseThrow(() -> new ApplicationException("Pais no encontrado"));
        repository.deleteById(id);
    }
}
