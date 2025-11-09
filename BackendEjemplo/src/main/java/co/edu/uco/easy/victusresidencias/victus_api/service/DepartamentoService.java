package co.edu.uco.easy.victusresidencias.victus_api.service;

import co.edu.uco.easy.victusresidencias.victus_api.entity.DepartamentoEntity;
import co.edu.uco.easy.victusresidencias.victus_api.repository.DepartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DepartamentoService {
    @Autowired
    private DepartamentoRepository repository;

    public List<DepartamentoEntity> findAll() {
        return repository.findAll();
    }

    public Optional<DepartamentoEntity> findById(UUID id) {
        return repository.findById(id);
    }

    public DepartamentoEntity save(DepartamentoEntity departamento) {
        return repository.save(departamento);
    }

    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    public boolean existsByNombre(String nombre) {
        return repository.existsByNombre(nombre);
    }

    public boolean existsByNombreAndPaisId(String nombre, UUID paisId) {
        return repository.existsByNombreAndPaisId(nombre, paisId);
    }
}