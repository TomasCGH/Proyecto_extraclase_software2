package co.edu.uco.backendvictus.infrastructure.secondary.repository;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.TestPropertySource;

import co.edu.uco.backendvictus.infrastructure.secondary.entity.ViviendaEntity;
import reactor.test.StepVerifier;

@DataR2dbcTest
@TestPropertySource(properties = {
        "spring.r2dbc.url=r2dbc:h2:mem:///testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
        "spring.r2dbc.username=sa",
        "spring.r2dbc.password="
})
class ViviendaReactiveRepositoryTest {

    @Autowired
    private ViviendaReactiveRepository repository;

    @Autowired
    private DatabaseClient databaseClient;

    @BeforeEach
    void setUp() {
        databaseClient.sql("DROP TABLE IF EXISTS vivienda").fetch().rowsUpdated().block();
        databaseClient.sql("CREATE TABLE vivienda (" +
                "id UUID PRIMARY KEY, " +
                "numero VARCHAR(10) NOT NULL, " +
                "tipo VARCHAR(30) NOT NULL, " +
                "estado VARCHAR(30) NOT NULL, " +
                "conjunto_id UUID NOT NULL, " +
                "metadatos JSON NULL)")
                .fetch().rowsUpdated().block();
        databaseClient.sql("CREATE UNIQUE INDEX IF NOT EXISTS uk_vivienda_numero_conjunto ON vivienda(numero, conjunto_id)")
                .fetch().rowsUpdated().block();
    }

    @Test
    void shouldSaveAndVerifyExistence() {
        final ViviendaEntity entity = new ViviendaEntity();
        entity.setId(UUID.randomUUID());
        entity.setNumero("A-101");
        entity.setTipo("APARTAMENTO");
        entity.setEstado("ACTIVA");
        entity.setConjuntoId(UUID.randomUUID());

        StepVerifier.create(repository.save(entity))
                .expectNextMatches(saved -> saved.getNumero().equals("A-101"))
                .verifyComplete();

        StepVerifier.create(repository.existsByNumeroAndConjuntoId(entity.getNumero(), entity.getConjuntoId()))
                .expectNext(Boolean.TRUE)
                .verifyComplete();
    }
}
