package co.edu.uco.backendvictus.application.usecase.vivienda;

import java.util.UUID;

import co.edu.uco.backendvictus.application.dto.vivienda.ViviendaCreateRequest;
import co.edu.uco.backendvictus.application.dto.vivienda.ViviendaResponse;
import reactor.core.publisher.Mono;

public interface RegistrarViviendaUseCase {

    Mono<ViviendaResponse> execute(ViviendaCreateRequest request, UUID principalId, String traceId);
}
