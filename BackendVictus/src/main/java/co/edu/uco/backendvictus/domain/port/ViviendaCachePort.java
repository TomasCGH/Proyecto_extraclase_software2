package co.edu.uco.backendvictus.domain.port;

import java.util.UUID;

import co.edu.uco.backendvictus.domain.model.Vivienda;
import reactor.core.publisher.Mono;

public interface ViviendaCachePort {

    Mono<Void> upsert(Vivienda vivienda);

    Mono<Vivienda> get(UUID id);
}
