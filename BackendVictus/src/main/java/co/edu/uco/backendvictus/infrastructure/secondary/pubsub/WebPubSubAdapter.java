package co.edu.uco.backendvictus.infrastructure.secondary.pubsub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.azure.messaging.webpubsub.WebPubSubAsyncServiceClient;
import com.azure.messaging.webpubsub.WebPubSubServiceClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.uco.backendvictus.crosscutting.exception.InfrastructureException;
import co.edu.uco.backendvictus.domain.port.RealtimePort;
import reactor.core.publisher.Mono;

@Component
public class WebPubSubAdapter implements RealtimePort {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebPubSubAdapter.class);

    private final WebPubSubAsyncServiceClient client;
    private final String hubName;
    private final ObjectMapper objectMapper;

    public WebPubSubAdapter(final WebPubSubServiceClientBuilder builder,
            @Value("${azure.webpubsub.hub}") final String hubName, final ObjectMapper objectMapper) {
        this.client = builder.buildAsyncClient();
        this.hubName = hubName;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> broadcast(final String channel, final Object payload) {
        try {
            final String message = objectMapper.writeValueAsString(payload);
            LOGGER.debug("Enviando mensaje en WebPubSub hub={} channel={}", hubName, channel);
            return client.sendToAll(message, channel).then();
        } catch (final JsonProcessingException exception) {
            return Mono.error(new InfrastructureException("Error serializando mensaje WebPubSub", exception));
        }
    }
}
