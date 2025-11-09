package co.edu.uco.backendvictus.application.usecase.pais;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.backendvictus.application.dto.pais.PaisResponse;
import co.edu.uco.backendvictus.application.dto.pais.PaisUpdateRequest;
import co.edu.uco.backendvictus.application.mapper.PaisApplicationMapper;
import co.edu.uco.backendvictus.crosscutting.exception.ApplicationException;
import co.edu.uco.backendvictus.domain.model.Pais;
import co.edu.uco.backendvictus.domain.port.PaisRepository;

@Service
public class UpdatePaisUseCase implements UseCase<PaisUpdateRequest, PaisResponse> {

    private final PaisRepository repository;

    public UpdatePaisUseCase(final PaisRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public PaisResponse execute(final PaisUpdateRequest request) {
        repository.findById(request.id())
                .orElseThrow(() -> new ApplicationException("Pais no encontrado"));

        final Pais actualizado = PaisApplicationMapper.toDomain(request);
        final Pais persisted = repository.save(actualizado);
        return PaisApplicationMapper.toResponse(persisted);
    }
}
