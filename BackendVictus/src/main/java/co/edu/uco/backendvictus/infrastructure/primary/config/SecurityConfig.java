package co.edu.uco.backendvictus.infrastructure.primary.config;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.AuthorizationContext;

import co.edu.uco.backendvictus.domain.port.ReactiveRoleService;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final ReactiveRoleService roleService;

    public SecurityConfig(final ReactiveRoleService roleService) {
        this.roleService = roleService;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(final ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(registry -> registry
                        .pathMatchers(HttpMethod.POST, "/v1/viviendas/**").access(adminOnly())
                        .pathMatchers(HttpMethod.DELETE, "/v1/viviendas/**").access(adminOnly())
                        .pathMatchers(HttpMethod.GET, "/v1/**").authenticated()
                        .anyExchange().permitAll())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {}))
                .build();
    }

    private ReactiveAuthorizationManager<AuthorizationContext> adminOnly() {
        return (authenticationMono, context) -> authenticationMono
                .flatMap(this::extractUserId)
                .flatMap(userId -> roleService.hasRole(userId, "ADMIN"))
                .map(hasRole -> new AuthorizationDecision(Boolean.TRUE.equals(hasRole)))
                .defaultIfEmpty(new AuthorizationDecision(false));
    }

    private Mono<UUID> extractUserId(final Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken jwtAuthentication) {
            final Jwt jwt = jwtAuthentication.getToken();
            try {
                return Mono.just(UUID.fromString(jwt.getSubject()));
            } catch (final IllegalArgumentException exception) {
                return Mono.error(new AccessDeniedException("El token JWT no contiene un subject valido"));
            }
        }
        return Mono.error(new AccessDeniedException("Autenticacion no soportada"));
    }
}
