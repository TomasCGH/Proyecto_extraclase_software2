package co.edu.uco.backendvictus.infrastructure.secondary.repository;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import co.edu.uco.backendvictus.domain.model.Vivienda;
import co.edu.uco.backendvictus.domain.port.ViviendaRepository;
import co.edu.uco.backendvictus.infrastructure.secondary.mapper.ViviendaEntityMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class PostgresViviendaRepositoryAdapter implements ViviendaRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostgresViviendaRepositoryAdapter.class);

    private final ViviendaReactiveRepository viviendaReactiveRepository;
    private final ViviendaEntityMapper mapper;

    public PostgresViviendaRepositoryAdapter(final ViviendaReactiveRepository viviendaReactiveRepository,
            final ViviendaEntityMapper mapper) {
        this.viviendaReactiveRepository = viviendaReactiveRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Vivienda> save(final Vivienda vivienda) {
        LOGGER.debug("Persistiendo vivienda {}", vivienda.getNumero());
        return viviendaReactiveRepository.save(mapper.toEntity(vivienda))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Boolean> existsByNumeroAndConjuntoId(final String numero, final UUID conjuntoId) {
        return viviendaReactiveRepository.existsByNumeroAndConjuntoId(numero, conjuntoId);
    }

    @Override
    public Mono<Vivienda> findById(final UUID id) {
        return viviendaReactiveRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Vivienda> findAllByConjuntoId(final UUID conjuntoId) {
        return viviendaReactiveRepository.findAllByConjuntoId(conjuntoId)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(final UUID id) {
        return viviendaReactiveRepository.deleteById(id);
    }
}
