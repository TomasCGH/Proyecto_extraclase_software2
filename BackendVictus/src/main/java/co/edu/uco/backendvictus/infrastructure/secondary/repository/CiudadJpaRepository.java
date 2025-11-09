package co.edu.uco.backendvictus.infrastructure.secondary.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.uco.backendvictus.infrastructure.secondary.entity.CiudadJpaEntity;

public interface CiudadJpaRepository extends JpaRepository<CiudadJpaEntity, UUID> {
}
