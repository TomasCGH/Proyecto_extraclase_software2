package co.edu.uco.backendvictus.application.usecase.vivienda;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.backendvictus.application.dto.vivienda.ViviendaResponse;
import co.edu.uco.backendvictus.application.mapper.ViviendaMapper;
import co.edu.uco.backendvictus.domain.port.ViviendaCachePort;
import co.edu.uco.backendvictus.domain.port.ViviendaRepository;
import reactor.core.publisher.Mono;

@Service
public class ObtenerViviendaUseCaseImpl implements ObtenerViviendaUseCase {

    private final ViviendaRepository viviendaRepository;
    private final ViviendaCachePort viviendaCachePort;
    private final ViviendaMapper mapper;

    public ObtenerViviendaUseCaseImpl(final ViviendaRepository viviendaRepository,
            final ViviendaCachePort viviendaCachePort, final ViviendaMapper mapper) {
        this.viviendaRepository = viviendaRepository;
        this.viviendaCachePort = viviendaCachePort;
        this.mapper = mapper;
    }

    @Override
    public Mono<ViviendaResponse> execute(final UUID id) {
        return viviendaCachePort.get(id)
                .switchIfEmpty(viviendaRepository.findById(id))
                .map(mapper::toResponse);
    }
}
