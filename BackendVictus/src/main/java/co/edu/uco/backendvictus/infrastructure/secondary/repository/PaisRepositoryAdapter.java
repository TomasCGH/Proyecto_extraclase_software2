package co.edu.uco.backendvictus.infrastructure.secondary.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import co.edu.uco.backendvictus.domain.model.Pais;
import co.edu.uco.backendvictus.domain.port.PaisRepository;
import co.edu.uco.backendvictus.infrastructure.secondary.entity.PaisJpaEntity;
import co.edu.uco.backendvictus.infrastructure.secondary.mapper.PaisEntityMapper;

@Repository
public class PaisRepositoryAdapter implements PaisRepository {

    private final PaisJpaRepository repository;

    public PaisRepositoryAdapter(final PaisJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Pais save(final Pais pais) {
        final PaisJpaEntity entity = PaisEntityMapper.toEntity(pais);
        final PaisJpaEntity saved = repository.save(entity);
        return PaisEntityMapper.toDomain(saved);
    }

    @Override
    public Optional<Pais> findById(final UUID id) {
        return repository.findById(id).map(PaisEntityMapper::toDomain);
    }

    @Override
    public List<Pais> findAll() {
        return repository.findAll().stream().map(PaisEntityMapper::toDomain).toList();
    }

    @Override
    public void deleteById(final UUID id) {
        repository.deleteById(id);
    }
}
