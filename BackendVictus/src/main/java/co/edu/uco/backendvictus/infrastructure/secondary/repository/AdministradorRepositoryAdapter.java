package co.edu.uco.backendvictus.infrastructure.secondary.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import co.edu.uco.backendvictus.domain.model.Administrador;
import co.edu.uco.backendvictus.domain.port.AdministradorRepository;
import co.edu.uco.backendvictus.infrastructure.secondary.entity.AdministradorJpaEntity;
import co.edu.uco.backendvictus.infrastructure.secondary.mapper.AdministradorEntityMapper;

@Repository
public class AdministradorRepositoryAdapter implements AdministradorRepository {

    private final AdministradorJpaRepository administradorJpaRepository;

    public AdministradorRepositoryAdapter(final AdministradorJpaRepository administradorJpaRepository) {
        this.administradorJpaRepository = administradorJpaRepository;
    }

    @Override
    public Administrador save(final Administrador administrador) {
        final AdministradorJpaEntity entity = AdministradorEntityMapper.toEntity(administrador);
        final AdministradorJpaEntity saved = administradorJpaRepository.save(entity);
        return AdministradorEntityMapper.toDomain(saved);
    }

    @Override
    public Optional<Administrador> findById(final UUID id) {
        return administradorJpaRepository.findById(id).map(AdministradorEntityMapper::toDomain);
    }

    @Override
    public List<Administrador> findAll() {
        return administradorJpaRepository.findAll().stream().map(AdministradorEntityMapper::toDomain).toList();
    }

    @Override
    public void deleteById(final UUID id) {
        administradorJpaRepository.deleteById(id);
    }
}
