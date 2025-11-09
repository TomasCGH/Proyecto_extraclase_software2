package co.edu.uco.easy.victusresidencias.victus_api.service;

import co.edu.uco.easy.victusresidencias.victus_api.entity.TipoIdentificacionEntity;
import co.edu.uco.easy.victusresidencias.victus_api.repository.TipoIdentificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TipoIdentificacionService {
    @Autowired
    private TipoIdentificacionRepository repository;

    public List<TipoIdentificacionEntity> findAll() {
        return repository.findAll();
    }

    public Optional<TipoIdentificacionEntity> findById(UUID id) {
        return repository.findById(id);
    }

    public TipoIdentificacionEntity save(TipoIdentificacionEntity tipoIdentificacion) {
        return repository.save(tipoIdentificacion);
    }

    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    public boolean existsByNombre(String nombre) {
        return repository.existsByNombre(nombre);
    }
}