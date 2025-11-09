package co.edu.uco.backendvictus.infrastructure.secondary.cosmos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.CosmosAsyncContainer;
import com.azure.cosmos.models.CosmosItemRequestOptions;
import com.azure.cosmos.models.PartitionKey;

import co.edu.uco.backendvictus.domain.model.Vivienda;
import co.edu.uco.backendvictus.domain.port.ViviendaCachePort;
import reactor.core.publisher.Mono;

@Component
public class CosmosViviendaRepositoryAdapter implements ViviendaCachePort {

    private static final Logger LOGGER = LoggerFactory.getLogger(CosmosViviendaRepositoryAdapter.class);

    private final CosmosAsyncContainer container;

    public CosmosViviendaRepositoryAdapter(final CosmosAsyncClient cosmosAsyncClient,
            @Value("${azure.cosmos.database}") final String databaseName,
            @Value("${azure.cosmos.container}") final String containerName) {
        this.container = cosmosAsyncClient.getDatabase(databaseName).getContainer(containerName);
    }

    @Override
    public Mono<Void> upsert(final Vivienda vivienda) {
        LOGGER.debug("Replicando vivienda {} en Cosmos", vivienda.getId());
        return container.upsertItem(vivienda, new PartitionKey(vivienda.getConjuntoId().toString()),
                new CosmosItemRequestOptions())
                .then();
    }

    @Override
    public Mono<Vivienda> get(final java.util.UUID id) {
        LOGGER.trace("Consulta de cache para vivienda {} - operacion delegada a Postgres", id);
        return Mono.empty();
    }
}
