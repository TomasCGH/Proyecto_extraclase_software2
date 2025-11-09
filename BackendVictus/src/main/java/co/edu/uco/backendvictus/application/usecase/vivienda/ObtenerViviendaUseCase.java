package co.edu.uco.backendvictus.application.usecase.vivienda;

import java.util.UUID;

import co.edu.uco.backendvictus.application.dto.vivienda.ViviendaResponse;
import reactor.core.publisher.Mono;

public interface ObtenerViviendaUseCase {

    Mono<ViviendaResponse> execute(UUID id);
}
