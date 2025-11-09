package co.edu.uco.backendvictus.application.usecase.vivienda;

import java.util.UUID;

import reactor.core.publisher.Mono;

public interface EliminarViviendaUseCase {

    Mono<Void> execute(UUID viviendaId);
}
