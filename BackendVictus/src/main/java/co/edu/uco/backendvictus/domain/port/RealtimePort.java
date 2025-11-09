package co.edu.uco.backendvictus.domain.port;

import reactor.core.publisher.Mono;

public interface RealtimePort {

    Mono<Void> broadcast(String channel, Object payload);
}
