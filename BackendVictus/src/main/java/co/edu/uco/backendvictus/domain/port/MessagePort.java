package co.edu.uco.backendvictus.domain.port;

import reactor.core.publisher.Mono;

public interface MessagePort {

    Mono<Void> publish(String eventType, Object payload, String traceId);
}
