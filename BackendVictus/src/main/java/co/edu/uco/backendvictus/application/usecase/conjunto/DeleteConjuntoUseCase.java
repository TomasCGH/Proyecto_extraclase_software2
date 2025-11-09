package co.edu.uco.backendvictus.application.usecase.conjunto;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.backendvictus.crosscutting.exception.ApplicationException;
import co.edu.uco.backendvictus.domain.port.ConjuntoResidencialRepository;

@Service
public class DeleteConjuntoUseCase {

    private final ConjuntoResidencialRepository conjuntoRepository;

    public DeleteConjuntoUseCase(final ConjuntoResidencialRepository conjuntoRepository) {
        this.conjuntoRepository = conjuntoRepository;
    }

    @Transactional
    public void execute(final UUID id) {
        conjuntoRepository.findById(id)
                .orElseThrow(() -> new ApplicationException("Conjunto residencial no encontrado"));
        conjuntoRepository.deleteById(id);
    }
}
