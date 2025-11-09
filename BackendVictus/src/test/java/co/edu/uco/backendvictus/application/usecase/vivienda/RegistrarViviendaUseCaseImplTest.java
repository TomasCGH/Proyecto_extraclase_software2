package co.edu.uco.backendvictus.application.usecase.vivienda;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import co.edu.uco.backendvictus.application.dto.vivienda.ViviendaCreateRequest;
import co.edu.uco.backendvictus.application.mapper.ViviendaMapper;
import co.edu.uco.backendvictus.domain.model.Vivienda;
import co.edu.uco.backendvictus.domain.port.MessagePort;
import co.edu.uco.backendvictus.domain.port.ReactiveRoleService;
import co.edu.uco.backendvictus.domain.port.RealtimePort;
import co.edu.uco.backendvictus.domain.port.ViviendaCachePort;
import co.edu.uco.backendvictus.domain.port.ViviendaRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class RegistrarViviendaUseCaseImplTest {

    @Mock
    private ViviendaRepository viviendaRepository;
    @Mock
    private ViviendaCachePort viviendaCachePort;
    @Mock
    private MessagePort messagePort;
    @Mock
    private RealtimePort realtimePort;
    @Mock
    private ReactiveRoleService roleService;

    private ViviendaMapper mapper;

    @InjectMocks
    private RegistrarViviendaUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        mapper = new ViviendaMapper();
        useCase = new RegistrarViviendaUseCaseImpl(viviendaRepository, viviendaCachePort, messagePort, realtimePort,
                roleService, mapper);
    }

    @Test
    void shouldRegisterViviendaSuccessfully() {
        final ViviendaCreateRequest request = new ViviendaCreateRequest("A-101", "APARTAMENTO", "OCUPADA",
                UUID.randomUUID(), Map.of("torre", "1"));
        final UUID principalId = UUID.randomUUID();
        final Vivienda vivienda = mapper.toDomain(UUID.randomUUID(), request);

        when(roleService.hasRole(principalId, "ADMIN")).thenReturn(Mono.just(Boolean.TRUE));
        when(viviendaRepository.existsByNumeroAndConjuntoId(request.numero(), request.conjuntoId()))
                .thenReturn(Mono.just(Boolean.FALSE));
        when(viviendaRepository.save(any(Vivienda.class))).thenReturn(Mono.just(vivienda));
        when(viviendaCachePort.upsert(any(Vivienda.class))).thenReturn(Mono.empty());
        when(messagePort.publish(any(), any(), any())).thenReturn(Mono.empty());
        when(realtimePort.broadcast(any(), any())).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(request, principalId, "trace"))
                .expectNextMatches(response -> response.numero().equals("A-101"))
                .verifyComplete();
    }

    @Test
    void shouldFailWhenUserIsNotAdmin() {
        final ViviendaCreateRequest request = new ViviendaCreateRequest("A-102", "CASA", "NO_OCUPADA",
                UUID.randomUUID(), null);
        final UUID principalId = UUID.randomUUID();

        when(roleService.hasRole(principalId, "ADMIN")).thenReturn(Mono.just(Boolean.FALSE));

        StepVerifier.create(useCase.execute(request, principalId, "trace"))
                .expectError(AccessDeniedException.class)
                .verify();
    }
}
