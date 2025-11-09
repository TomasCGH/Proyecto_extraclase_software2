package co.edu.uco.backendvictus.infrastructure.secondary.repository;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;

import co.edu.uco.backendvictus.domain.port.ReactiveRoleService;
import reactor.core.publisher.Mono;

@Component
public class ReactiveRoleServiceAdapter implements ReactiveRoleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReactiveRoleServiceAdapter.class);
    private final DatabaseClient databaseClient;

    public ReactiveRoleServiceAdapter(final DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public Mono<Boolean> hasRole(final UUID userId, final String role) {
        return databaseClient.sql(
                "SELECT CASE WHEN COUNT(1) > 0 THEN TRUE ELSE FALSE END as has_role FROM usuario_rol WHERE usuario_id = :userId AND rol = :role")
                .bind("userId", userId)
                .bind("role", role)
                .map(row -> row.get("has_role", Boolean.class))
                .one()
                .doOnNext(result -> LOGGER.debug("Usuario {} tiene rol {} = {}", userId, role, result))
                .defaultIfEmpty(Boolean.FALSE);
    }
}
