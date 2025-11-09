package co.edu.uco.backendvictus.application.usecase.ciudad;

import co.edu.uco.backendvictus.application.usecase.UseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.backendvictus.application.dto.ciudad.CiudadResponse;
import co.edu.uco.backendvictus.application.dto.ciudad.CiudadUpdateRequest;
import co.edu.uco.backendvictus.application.mapper.CiudadApplicationMapper;
import co.edu.uco.backendvictus.crosscutting.exception.ApplicationException;
import co.edu.uco.backendvictus.domain.model.Ciudad;
import co.edu.uco.backendvictus.domain.model.Departamento;
import co.edu.uco.backendvictus.domain.port.CiudadRepository;
import co.edu.uco.backendvictus.domain.port.DepartamentoRepository;

@Service
public class UpdateCiudadUseCase implements UseCase<CiudadUpdateRequest, CiudadResponse> {

    private final CiudadRepository ciudadRepository;
    private final DepartamentoRepository departamentoRepository;
    private final CiudadApplicationMapper mapper;

    public UpdateCiudadUseCase(final CiudadRepository ciudadRepository,
            final DepartamentoRepository departamentoRepository, final CiudadApplicationMapper mapper) {
        this.ciudadRepository = ciudadRepository;
        this.departamentoRepository = departamentoRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public CiudadResponse execute(final CiudadUpdateRequest request) {
        final Ciudad existente = ciudadRepository.findById(request.id())
                .orElseThrow(() -> new ApplicationException("Ciudad no encontrada"));

        final Departamento departamento = departamentoRepository.findById(request.departamentoId())
                .orElseThrow(() -> new ApplicationException("Departamento no encontrado"));

        final Ciudad actualizada = existente.update(request.nombre(), departamento, request.activo());
        final Ciudad persisted = ciudadRepository.save(actualizada);
        return mapper.toResponse(persisted);
    }
}
