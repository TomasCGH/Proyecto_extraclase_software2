package co.edu.uco.backendvictus.infrastructure.primary.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.test.web.reactive.server.WebTestClient;

import co.edu.uco.backendvictus.application.dto.vivienda.ViviendaCreateRequest;
import co.edu.uco.backendvictus.application.dto.vivienda.ViviendaResponse;
import co.edu.uco.backendvictus.application.usecase.vivienda.EliminarViviendaUseCase;
import co.edu.uco.backendvictus.application.usecase.vivienda.ListarViviendasPorConjuntoUseCase;
import co.edu.uco.backendvictus.application.usecase.vivienda.ObtenerViviendaUseCase;
import co.edu.uco.backendvictus.application.usecase.vivienda.RegistrarViviendaUseCase;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = ViviendaController.class)
@Import(ViviendaControllerTest.TestSecurityConfig.class)
class ViviendaControllerTest {

    @MockBean
    private RegistrarViviendaUseCase registrarViviendaUseCase;
    @MockBean
    private ObtenerViviendaUseCase obtenerViviendaUseCase;
    @MockBean
    private ListarViviendasPorConjuntoUseCase listarViviendasPorConjuntoUseCase;
    @MockBean
    private EliminarViviendaUseCase eliminarViviendaUseCase;

    private final WebTestClient webTestClient;

    ViviendaControllerTest(final WebTestClient webTestClient) {
        this.webTestClient = webTestClient.mutate().responseTimeout(Duration.ofSeconds(5)).build();
    }

    @Test
    void shouldCreateVivienda() {
        final UUID viviendaId = UUID.randomUUID();
        final ViviendaResponse response = new ViviendaResponse(viviendaId, "A-203", "APARTAMENTO", "ACTIVA",
                UUID.randomUUID(), Map.of("torre", "3"));

        when(registrarViviendaUseCase.execute(any(ViviendaCreateRequest.class), any(UUID.class), any()))
                .thenReturn(Mono.just(response));

        webTestClient.post()
                .uri("/v1/viviendas")
                .header("X-Trace-Id", "test-trace")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{" +
                        "\"numero\":\"A-203\"," +
                        "\"tipo\":\"APARTAMENTO\"," +
                        "\"estado\":\"ACTIVA\"," +
                        "\"conjuntoId\":\"" + UUID.randomUUID() + "\"," +
                        "\"metadatos\":{\"torre\":\"3\"}}")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().valueEquals("X-Trace-Id", "test-trace");
    }

    @Test
    void shouldListByConjunto() {
        when(listarViviendasPorConjuntoUseCase.execute(any(UUID.class)))
                .thenReturn(Flux.just(new ViviendaResponse(UUID.randomUUID(), "B-101", "CASA", "ACTIVA",
                        UUID.randomUUID(), null)));

        webTestClient.get()
                .uri("/v1/conjuntos/" + UUID.randomUUID() + "/viviendas")
                .exchange()
                .expectStatus().isOk();
    }

    static class TestSecurityConfig {
        @Bean
        SecurityWebFilterChain testSecurityFilterChain(final ServerHttpSecurity http) {
            return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                    .authorizeExchange(registry -> registry.anyExchange().permitAll())
                    .build();
        }
    }
}
