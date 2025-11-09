package co.edu.uco.easy.victusresidencias.victus_api.service;

import co.edu.uco.easy.victusresidencias.victus_api.entity.PaisEntity;
import co.edu.uco.easy.victusresidencias.victus_api.repository.PaisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaisService {
    @Autowired
    private PaisRepository repository;

    public List<PaisEntity> findAll() {
        return repository.findAll();
    }

    public Optional<PaisEntity> findById(UUID id) {
        return repository.findById(id);
    }

    public PaisEntity save(PaisEntity pais) {
        return repository.save(pais);
    }

    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    public boolean existsByNombre(String nombre) {
        return repository.existsByNombre(nombre);
    }
}