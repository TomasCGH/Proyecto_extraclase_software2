package co.edu.uco.backendvictus.application.usecase.administrador;

import co.edu.uco.backendvictus.application.usecase.UseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.backendvictus.application.dto.administrador.AdministradorResponse;
import co.edu.uco.backendvictus.application.dto.administrador.AdministradorUpdateRequest;
import co.edu.uco.backendvictus.application.mapper.AdministradorApplicationMapper;
import co.edu.uco.backendvictus.crosscutting.exception.ApplicationException;
import co.edu.uco.backendvictus.domain.model.Administrador;
import co.edu.uco.backendvictus.domain.port.AdministradorRepository;

@Service
public class UpdateAdministradorUseCase implements UseCase<AdministradorUpdateRequest, AdministradorResponse> {

    private final AdministradorRepository administradorRepository;
    private final AdministradorApplicationMapper mapper;

    public UpdateAdministradorUseCase(final AdministradorRepository administradorRepository,
            final AdministradorApplicationMapper mapper) {
        this.administradorRepository = administradorRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public AdministradorResponse execute(final AdministradorUpdateRequest request) {
        final Administrador existente = administradorRepository.findById(request.id())
                .orElseThrow(() -> new ApplicationException("Administrador no encontrado"));

        final Administrador actualizado = existente.update(request.primerNombre(), request.segundoNombres(),
                request.primerApellido(), request.segundoApellido(), request.email(), request.telefono(),
                request.activo());
        final Administrador persisted = administradorRepository.save(actualizado);
        return mapper.toResponse(persisted);
    }
}
