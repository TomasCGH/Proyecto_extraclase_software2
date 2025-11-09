package co.edu.uco.backendvictus.application.usecase.vivienda;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import co.edu.uco.backendvictus.application.dto.vivienda.ViviendaCreateRequest;
import co.edu.uco.backendvictus.application.dto.vivienda.ViviendaResponse;
import co.edu.uco.backendvictus.application.mapper.ViviendaMapper;
import co.edu.uco.backendvictus.crosscutting.exception.DomainException;
import co.edu.uco.backendvictus.crosscutting.helpers.Sanitizer;
import co.edu.uco.backendvictus.domain.model.Vivienda;
import co.edu.uco.backendvictus.domain.port.MessagePort;
import co.edu.uco.backendvictus.domain.port.ReactiveRoleService;
import co.edu.uco.backendvictus.domain.port.RealtimePort;
import co.edu.uco.backendvictus.domain.port.ViviendaCachePort;
import co.edu.uco.backendvictus.domain.port.ViviendaRepository;
import reactor.core.publisher.Mono;

@Service
public class RegistrarViviendaUseCaseImpl implements RegistrarViviendaUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrarViviendaUseCaseImpl.class);
    private static final String TRACE_ID_KEY = "traceId";

    private final ViviendaRepository viviendaRepository;
    private final ViviendaCachePort viviendaCachePort;
    private final MessagePort messagePort;
    private final RealtimePort realtimePort;
    private final ReactiveRoleService roleService;
    private final ViviendaMapper mapper;

    public RegistrarViviendaUseCaseImpl(final ViviendaRepository viviendaRepository,
            final ViviendaCachePort viviendaCachePort, final MessagePort messagePort,
            final RealtimePort realtimePort, final ReactiveRoleService roleService, final ViviendaMapper mapper) {
        this.viviendaRepository = viviendaRepository;
        this.viviendaCachePort = viviendaCachePort;
        this.messagePort = messagePort;
        this.realtimePort = realtimePort;
        this.roleService = roleService;
        this.mapper = mapper;
    }

    @Override
    public Mono<ViviendaResponse> execute(final ViviendaCreateRequest request, final UUID principalId,
            final String traceId) {
        final String effectiveTraceId = traceId != null ? traceId : UUID.randomUUID().toString();
        final ViviendaCreateRequest sanitizedRequest = sanitizeRequest(request);
        return Mono.defer(() -> {
            MDC.put(TRACE_ID_KEY, effectiveTraceId);
            LOGGER.info("Iniciando registro de vivienda - traceId={} numero={} conjunto={} principalId={}", effectiveTraceId,
                    sanitizedRequest.numero(), sanitizedRequest.conjuntoId(), principalId);
            return validateRole(principalId)
                    .then(validateUniqueNumero(sanitizedRequest))
                    .flatMap(number -> persistVivienda(sanitizedRequest))
                    .flatMap(saved -> replicateAndNotify(saved, effectiveTraceId))
                    .doOnSuccess(response -> LOGGER.info(
                            "Vivienda registrada exitosamente - traceId={} viviendaId={} numero={}", effectiveTraceId,
                            response.id(), response.numero()))
                    .doOnError(error -> LOGGER.error("Error registrando vivienda - traceId={}", effectiveTraceId, error))
                    .doFinally(signalType -> MDC.remove(TRACE_ID_KEY));
        });
    }

    private ViviendaCreateRequest sanitizeRequest(final ViviendaCreateRequest request) {
        return new ViviendaCreateRequest(Sanitizer.sanitize(request.numero()), Sanitizer.sanitize(request.tipo()),
                Sanitizer.sanitize(request.estado()), request.conjuntoId(), request.metadatos());
    }

    private Mono<Void> validateRole(final UUID principalId) {
        return roleService.hasRole(principalId, "ADMIN")
                .flatMap(hasRole -> Boolean.TRUE.equals(hasRole) ? Mono.<Void>empty()
                        : Mono.error(new AccessDeniedException("El usuario no tiene permisos de administrador")));
    }

    private Mono<String> validateUniqueNumero(final ViviendaCreateRequest request) {
        return viviendaRepository.existsByNumeroAndConjuntoId(request.numero(), request.conjuntoId())
                .flatMap(exists -> Boolean.TRUE.equals(exists)
                        ? Mono.error(new DomainException("Ya existe una vivienda con ese numero en el conjunto"))
                        : Mono.just(request.numero()));
    }

    private Mono<Vivienda> persistVivienda(final ViviendaCreateRequest request) {
        final UUID viviendaId = UUID.randomUUID();
        final Vivienda vivienda = mapper.toDomain(viviendaId, request);
        return viviendaRepository.save(vivienda);
    }

    private Mono<ViviendaResponse> replicateAndNotify(final Vivienda vivienda, final String traceId) {
        final Map<String, Object> payload = new java.util.LinkedHashMap<>();
        payload.put("id", vivienda.getId().toString());
        payload.put("numero", vivienda.getNumero());
        payload.put("tipo", vivienda.getTipo());
        payload.put("estado", vivienda.getEstado());
        payload.put("conjuntoId", vivienda.getConjuntoId().toString());
        payload.put("timestamp", Instant.now().toString());
        if (vivienda.getMetadatos() != null) {
            payload.put("metadatos", vivienda.getMetadatos());
        }
        final Map<String, Object> payload = Map.of(
                "id", vivienda.getId().toString(),
                "numero", vivienda.getNumero(),
                "tipo", vivienda.getTipo(),
                "estado", vivienda.getEstado(),
                "conjuntoId", vivienda.getConjuntoId().toString(),
                "timestamp", Instant.now().toString(),
                "metadatos", vivienda.getMetadatos());

        return viviendaCachePort.upsert(vivienda)
                .then(messagePort.publish("vivienda.creada", payload, traceId))
                .then(realtimePort.broadcast("viviendas", payload))
                .thenReturn(mapper.toResponse(vivienda));
    }
}
