package co.edu.uco.backendvictus.infrastructure.secondary.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderAsyncClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.uco.backendvictus.crosscutting.exception.InfrastructureException;
import co.edu.uco.backendvictus.domain.port.MessagePort;
import reactor.core.publisher.Mono;

@Component
public class ServiceBusMessageAdapter implements MessagePort {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceBusMessageAdapter.class);

    private final ServiceBusSenderAsyncClient senderClient;
    private final String topic;
    private final ObjectMapper objectMapper;

    public ServiceBusMessageAdapter(final ServiceBusClientBuilder clientBuilder,
            @Value("${azure.servicebus.topic-name}") final String topic, final ObjectMapper objectMapper) {
        this.topic = topic;
        this.objectMapper = objectMapper;
        this.senderClient = clientBuilder.sender().topicName(topic).buildAsyncClient();
    }

    @Override
    public Mono<Void> publish(final String eventType, final Object payload, final String traceId) {
        LOGGER.debug("Publicando evento {} en topic {}", eventType, topic);
        try {
            final String body = objectMapper.writeValueAsString(payload);
            final ServiceBusMessage message = new ServiceBusMessage(body);
            message.setSubject(eventType);
            message.getApplicationProperties().put("traceId", traceId);
            return senderClient.sendMessage(message).then();
        } catch (final JsonProcessingException exception) {
            return Mono.error(new InfrastructureException("Error serializando mensaje de vivienda", exception));
        }
    }
}
