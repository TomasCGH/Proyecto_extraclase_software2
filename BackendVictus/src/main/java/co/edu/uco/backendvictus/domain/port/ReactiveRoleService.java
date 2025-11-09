package co.edu.uco.backendvictus.domain.port;

import java.util.UUID;

import reactor.core.publisher.Mono;

public interface ReactiveRoleService {

    Mono<Boolean> hasRole(UUID userId, String role);
}
