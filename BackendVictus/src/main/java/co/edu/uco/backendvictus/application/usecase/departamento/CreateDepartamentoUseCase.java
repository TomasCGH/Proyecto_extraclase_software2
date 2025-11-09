package co.edu.uco.backendvictus.application.usecase.departamento;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.backendvictus.application.dto.departamento.DepartamentoCreateRequest;
import co.edu.uco.backendvictus.application.dto.departamento.DepartamentoResponse;
import co.edu.uco.backendvictus.application.mapper.DepartamentoApplicationMapper;
import co.edu.uco.backendvictus.application.usecase.UseCase;
import co.edu.uco.backendvictus.crosscutting.exception.ApplicationException;
import co.edu.uco.backendvictus.domain.model.Departamento;
import co.edu.uco.backendvictus.domain.model.Pais;
import co.edu.uco.backendvictus.domain.port.DepartamentoRepository;
import co.edu.uco.backendvictus.domain.port.PaisRepository;

@Service
public class CreateDepartamentoUseCase implements UseCase<DepartamentoCreateRequest, DepartamentoResponse> {

    private final DepartamentoRepository departamentoRepository;
    private final PaisRepository paisRepository;

    public CreateDepartamentoUseCase(final DepartamentoRepository departamentoRepository,
            final PaisRepository paisRepository) {
        this.departamentoRepository = departamentoRepository;
        this.paisRepository = paisRepository;
    }

    @Override
    @Transactional
    public DepartamentoResponse execute(final DepartamentoCreateRequest request) {
        final Pais pais = paisRepository.findById(request.paisId())
                .orElseThrow(() -> new ApplicationException("Pais no encontrado"));

        final Departamento departamento = Departamento.create(UUID.randomUUID(), request.nombre(), pais,
                request.activo());
        final Departamento persisted = departamentoRepository.save(departamento);
        return DepartamentoApplicationMapper.toResponse(persisted);
    }
}
