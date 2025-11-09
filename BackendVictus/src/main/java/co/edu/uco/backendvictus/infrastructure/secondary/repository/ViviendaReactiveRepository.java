package co.edu.uco.backendvictus.infrastructure.secondary.repository;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import co.edu.uco.backendvictus.infrastructure.secondary.entity.ViviendaEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ViviendaReactiveRepository extends ReactiveCrudRepository<ViviendaEntity, UUID> {

    @Query("SELECT EXISTS(SELECT 1 FROM vivienda WHERE numero = :numero AND conjunto_id = :conjuntoId)")
    Mono<Boolean> existsByNumeroAndConjuntoId(String numero, UUID conjuntoId);

    @Query("SELECT * FROM vivienda WHERE conjunto_id = :conjuntoId")
    Flux<ViviendaEntity> findAllByConjuntoId(UUID conjuntoId);
}
