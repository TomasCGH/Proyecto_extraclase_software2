package co.edu.uco.backendvictus.application.usecase.vivienda;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.backendvictus.application.dto.vivienda.ViviendaResponse;
import co.edu.uco.backendvictus.application.mapper.ViviendaMapper;
import co.edu.uco.backendvictus.domain.port.ViviendaRepository;
import reactor.core.publisher.Flux;

@Service
public class ListarViviendasPorConjuntoUseCaseImpl implements ListarViviendasPorConjuntoUseCase {

    private final ViviendaRepository viviendaRepository;
    private final ViviendaMapper mapper;

    public ListarViviendasPorConjuntoUseCaseImpl(final ViviendaRepository viviendaRepository,
            final ViviendaMapper mapper) {
        this.viviendaRepository = viviendaRepository;
        this.mapper = mapper;
    }

    @Override
    public Flux<ViviendaResponse> execute(final UUID conjuntoId) {
        return viviendaRepository.findAllByConjuntoId(conjuntoId)
                .map(mapper::toResponse);
    }
}
