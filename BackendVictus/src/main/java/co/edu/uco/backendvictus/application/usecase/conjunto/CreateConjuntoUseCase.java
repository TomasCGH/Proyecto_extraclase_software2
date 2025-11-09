package co.edu.uco.backendvictus.application.usecase.conjunto;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.backendvictus.application.dto.conjunto.ConjuntoCreateRequest;
import co.edu.uco.backendvictus.application.dto.conjunto.ConjuntoResponse;
import co.edu.uco.backendvictus.application.mapper.ConjuntoApplicationMapper;
import co.edu.uco.backendvictus.application.usecase.UseCase;
import co.edu.uco.backendvictus.crosscutting.exception.ApplicationException;
import co.edu.uco.backendvictus.domain.model.Administrador;
import co.edu.uco.backendvictus.domain.model.Ciudad;
import co.edu.uco.backendvictus.domain.model.ConjuntoResidencial;
import co.edu.uco.backendvictus.domain.port.AdministradorRepository;
import co.edu.uco.backendvictus.domain.port.CiudadRepository;
import co.edu.uco.backendvictus.domain.port.ConjuntoResidencialRepository;

@Service
public class CreateConjuntoUseCase implements UseCase<ConjuntoCreateRequest, ConjuntoResponse> {

    private final ConjuntoResidencialRepository conjuntoRepository;
    private final CiudadRepository ciudadRepository;
    private final AdministradorRepository administradorRepository;

    public CreateConjuntoUseCase(final ConjuntoResidencialRepository conjuntoRepository,
            final CiudadRepository ciudadRepository, final AdministradorRepository administradorRepository) {
        this.conjuntoRepository = conjuntoRepository;
        this.ciudadRepository = ciudadRepository;
        this.administradorRepository = administradorRepository;
    }

    @Override
    @Transactional
    public ConjuntoResponse execute(final ConjuntoCreateRequest request) {
        final Ciudad ciudad = ciudadRepository.findById(request.ciudadId())
                .orElseThrow(() -> new ApplicationException("Ciudad no encontrada"));
        final Administrador administrador = administradorRepository.findById(request.administradorId())
                .orElseThrow(() -> new ApplicationException("Administrador no encontrado"));

        final ConjuntoResidencial conjuntoResidencial = ConjuntoApplicationMapper.toDomain(UUID.randomUUID(), request,
                ciudad, administrador);
        final ConjuntoResidencial persisted = conjuntoRepository.save(conjuntoResidencial);
        return ConjuntoApplicationMapper.toResponse(persisted);
    }
}
