package co.edu.uco.backendvictus.application.usecase.ciudad;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.backendvictus.application.dto.ciudad.CiudadCreateRequest;
import co.edu.uco.backendvictus.application.dto.ciudad.CiudadResponse;
import co.edu.uco.backendvictus.application.mapper.CiudadApplicationMapper;
import co.edu.uco.backendvictus.application.usecase.UseCase;
import co.edu.uco.backendvictus.crosscutting.exception.ApplicationException;
import co.edu.uco.backendvictus.domain.model.Ciudad;
import co.edu.uco.backendvictus.domain.model.Departamento;
import co.edu.uco.backendvictus.domain.port.CiudadRepository;
import co.edu.uco.backendvictus.domain.port.DepartamentoRepository;

@Service
public class CreateCiudadUseCase implements UseCase<CiudadCreateRequest, CiudadResponse> {

    private final CiudadRepository ciudadRepository;
    private final DepartamentoRepository departamentoRepository;

    public CreateCiudadUseCase(final CiudadRepository ciudadRepository,
            final DepartamentoRepository departamentoRepository) {
        this.ciudadRepository = ciudadRepository;
        this.departamentoRepository = departamentoRepository;
    }

    @Override
    @Transactional
    public CiudadResponse execute(final CiudadCreateRequest request) {
        final Departamento departamento = departamentoRepository.findById(request.departamentoId())
                .orElseThrow(() -> new ApplicationException("Departamento no encontrado"));

        final Ciudad ciudad = Ciudad.create(UUID.randomUUID(), request.nombre(), departamento, request.activo());
        final Ciudad persisted = ciudadRepository.save(ciudad);
        return CiudadApplicationMapper.toResponse(persisted);
    }
}
