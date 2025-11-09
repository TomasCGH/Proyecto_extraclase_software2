package co.edu.uco.backendvictus.domain.port;

import java.util.UUID;

import co.edu.uco.backendvictus.domain.model.Vivienda;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ViviendaRepository {

    Mono<Vivienda> save(Vivienda vivienda);

    Mono<Boolean> existsByNumeroAndConjuntoId(String numero, UUID conjuntoId);

    Mono<Vivienda> findById(UUID id);

    Flux<Vivienda> findAllByConjuntoId(UUID conjuntoId);

    Mono<Void> deleteById(UUID id);
}
