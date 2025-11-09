package co.edu.uco.backendvictus.application.usecase.departamento;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.backendvictus.application.dto.departamento.DepartamentoResponse;
import co.edu.uco.backendvictus.application.dto.departamento.DepartamentoUpdateRequest;
import co.edu.uco.backendvictus.application.mapper.DepartamentoApplicationMapper;
import co.edu.uco.backendvictus.crosscutting.exception.ApplicationException;
import co.edu.uco.backendvictus.domain.model.Departamento;
import co.edu.uco.backendvictus.domain.model.Pais;
import co.edu.uco.backendvictus.domain.port.DepartamentoRepository;
import co.edu.uco.backendvictus.domain.port.PaisRepository;

@Service
public class UpdateDepartamentoUseCase implements UseCase<DepartamentoUpdateRequest, DepartamentoResponse> {

    private final DepartamentoRepository departamentoRepository;
    private final PaisRepository paisRepository;

    public UpdateDepartamentoUseCase(final DepartamentoRepository departamentoRepository,
            final PaisRepository paisRepository) {
        this.departamentoRepository = departamentoRepository;
        this.paisRepository = paisRepository;
    }

    @Override
    @Transactional
    public DepartamentoResponse execute(final DepartamentoUpdateRequest request) {
        final Departamento existente = departamentoRepository.findById(request.id())
                .orElseThrow(() -> new ApplicationException("Departamento no encontrado"));

        final Pais pais = paisRepository.findById(request.paisId())
                .orElseThrow(() -> new ApplicationException("Pais no encontrado"));

        final Departamento actualizado = existente.update(request.nombre(), pais, request.activo());
        final Departamento persisted = departamentoRepository.save(actualizado);
        return DepartamentoApplicationMapper.toResponse(persisted);
    }
}
