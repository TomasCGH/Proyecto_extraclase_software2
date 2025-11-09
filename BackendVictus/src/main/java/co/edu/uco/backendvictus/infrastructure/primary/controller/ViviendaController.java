package co.edu.uco.backendvictus.infrastructure.primary.controller;

import java.net.URI;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import co.edu.uco.backendvictus.application.dto.vivienda.ViviendaCreateRequest;
import co.edu.uco.backendvictus.application.dto.vivienda.ViviendaResponse;
import co.edu.uco.backendvictus.application.usecase.vivienda.EliminarViviendaUseCase;
import co.edu.uco.backendvictus.application.usecase.vivienda.ListarViviendasPorConjuntoUseCase;
import co.edu.uco.backendvictus.application.usecase.vivienda.ObtenerViviendaUseCase;
import co.edu.uco.backendvictus.application.usecase.vivienda.RegistrarViviendaUseCase;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1")
@Validated
public class ViviendaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViviendaController.class);

    private final RegistrarViviendaUseCase registrarViviendaUseCase;
    private final ObtenerViviendaUseCase obtenerViviendaUseCase;
    private final ListarViviendasPorConjuntoUseCase listarViviendasPorConjuntoUseCase;
    private final EliminarViviendaUseCase eliminarViviendaUseCase;

    public ViviendaController(final RegistrarViviendaUseCase registrarViviendaUseCase,
            final ObtenerViviendaUseCase obtenerViviendaUseCase,
            final ListarViviendasPorConjuntoUseCase listarViviendasPorConjuntoUseCase,
            final EliminarViviendaUseCase eliminarViviendaUseCase) {
        this.registrarViviendaUseCase = registrarViviendaUseCase;
        this.obtenerViviendaUseCase = obtenerViviendaUseCase;
        this.listarViviendasPorConjuntoUseCase = listarViviendasPorConjuntoUseCase;
        this.eliminarViviendaUseCase = eliminarViviendaUseCase;
    }

    @PostMapping("/viviendas")
    public Mono<ResponseEntity<ViviendaResponse>> crearVivienda(
            @Validated @RequestBody final Mono<ViviendaCreateRequest> requestMono,
            final ServerWebExchange exchange) {
        final String traceId = resolveTraceId(exchange);
        LOGGER.debug("Solicitud de creacion de vivienda - traceId={}", traceId);
        return Mono.zip(requestMono, extractPrincipalId())
                .flatMap(tuple -> registrarViviendaUseCase
                        .execute(tuple.getT1(), tuple.getT2(), traceId)
                        .map(response -> ResponseEntity.created(location(exchange, response.id()))
                                .header("X-Trace-Id", traceId)
                                .body(response)));
    }

    @GetMapping("/viviendas/{id}")
    public Mono<ResponseEntity<ViviendaResponse>> obtenerVivienda(@PathVariable("id") final UUID id) {
        return obtenerViviendaUseCase.execute(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/conjuntos/{conjuntoId}/viviendas")
    public Flux<ViviendaResponse> listarViviendas(@PathVariable("conjuntoId") final UUID conjuntoId) {
        return listarViviendasPorConjuntoUseCase.execute(conjuntoId);
    }

    @DeleteMapping("/viviendas/{id}")
    public Mono<ResponseEntity<Void>> eliminarVivienda(@PathVariable("id") final UUID id) {
        return eliminarViviendaUseCase.execute(id)
                .thenReturn(ResponseEntity.noContent().build());
    }

    private Mono<UUID> extractPrincipalId() {
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> securityContext.getAuthentication())
                .flatMap(authentication -> {
                    if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
                        final Jwt jwt = jwtAuthenticationToken.getToken();
                        try {
                            return Mono.just(UUID.fromString(jwt.getSubject()));
                        } catch (final IllegalArgumentException exception) {
                            return Mono.error(new IllegalStateException("El subject del token no es un UUID valido"));
                        }
                    }
                    return Mono.error(new IllegalStateException("No se pudo extraer el usuario del token"));
                });
    }

    private String resolveTraceId(final ServerWebExchange exchange) {
        final String providedTraceId = exchange.getRequest().getHeaders().getFirst("X-Trace-Id");
        return providedTraceId != null ? providedTraceId : exchange.getRequest().getId();
    }

    private URI location(final ServerWebExchange exchange, final UUID id) {
        return URI.create(exchange.getRequest().getURI().toString() + "/" + id);
    }
}
