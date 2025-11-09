package co.edu.uco.backendvictus.infrastructure.secondary.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import co.edu.uco.backendvictus.domain.model.Departamento;
import co.edu.uco.backendvictus.domain.port.DepartamentoRepository;
import co.edu.uco.backendvictus.infrastructure.secondary.entity.DepartamentoJpaEntity;
import co.edu.uco.backendvictus.infrastructure.secondary.entity.PaisJpaEntity;
import co.edu.uco.backendvictus.infrastructure.secondary.mapper.DepartamentoEntityMapper;

@Repository
public class DepartamentoRepositoryAdapter implements DepartamentoRepository {

    private final DepartamentoJpaRepository departamentoJpaRepository;
    private final PaisJpaRepository paisJpaRepository;

    public DepartamentoRepositoryAdapter(final DepartamentoJpaRepository departamentoJpaRepository,
            final PaisJpaRepository paisJpaRepository) {
        this.departamentoJpaRepository = departamentoJpaRepository;
        this.paisJpaRepository = paisJpaRepository;
    }

    @Override
    public Departamento save(final Departamento departamento) {
        final PaisJpaEntity paisReference = paisJpaRepository.getReferenceById(departamento.getPais().getId());
        final DepartamentoJpaEntity entity = new DepartamentoJpaEntity(departamento.getId(), paisReference,
                departamento.getNombre(), departamento.isActivo());
        final DepartamentoJpaEntity saved = departamentoJpaRepository.save(entity);
        return DepartamentoEntityMapper.toDomain(saved);
    }

    @Override
    public Optional<Departamento> findById(final UUID id) {
        return departamentoJpaRepository.findById(id).map(DepartamentoEntityMapper::toDomain);
    }

    @Override
    public List<Departamento> findAll() {
        return departamentoJpaRepository.findAll().stream().map(DepartamentoEntityMapper::toDomain).toList();
    }

    @Override
    public void deleteById(final UUID id) {
        departamentoJpaRepository.deleteById(id);
    }
}
