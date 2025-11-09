package co.edu.uco.backendvictus.infrastructure.secondary.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import co.edu.uco.backendvictus.domain.model.Ciudad;
import co.edu.uco.backendvictus.domain.port.CiudadRepository;
import co.edu.uco.backendvictus.infrastructure.secondary.entity.CiudadJpaEntity;
import co.edu.uco.backendvictus.infrastructure.secondary.entity.DepartamentoJpaEntity;
import co.edu.uco.backendvictus.infrastructure.secondary.mapper.CiudadEntityMapper;

@Repository
public class CiudadRepositoryAdapter implements CiudadRepository {

    private final CiudadJpaRepository ciudadJpaRepository;
    private final DepartamentoJpaRepository departamentoJpaRepository;

    public CiudadRepositoryAdapter(final CiudadJpaRepository ciudadJpaRepository,
            final DepartamentoJpaRepository departamentoJpaRepository) {
        this.ciudadJpaRepository = ciudadJpaRepository;
        this.departamentoJpaRepository = departamentoJpaRepository;
    }

    @Override
    public Ciudad save(final Ciudad ciudad) {
        final DepartamentoJpaEntity departamento =
                departamentoJpaRepository.getReferenceById(ciudad.getDepartamento().getId());
        final CiudadJpaEntity entity = new CiudadJpaEntity(ciudad.getId(), departamento, ciudad.getNombre(),
                ciudad.isActivo());
        final CiudadJpaEntity saved = ciudadJpaRepository.save(entity);
        return CiudadEntityMapper.toDomain(saved);
    }

    @Override
    public Optional<Ciudad> findById(final UUID id) {
        return ciudadJpaRepository.findById(id).map(CiudadEntityMapper::toDomain);
    }

    @Override
    public List<Ciudad> findAll() {
        return ciudadJpaRepository.findAll().stream().map(CiudadEntityMapper::toDomain).toList();
    }

    @Override
    public void deleteById(final UUID id) {
        ciudadJpaRepository.deleteById(id);
    }
}
