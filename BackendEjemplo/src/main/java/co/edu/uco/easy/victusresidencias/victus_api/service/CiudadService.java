package co.edu.uco.easy.victusresidencias.victus_api.service;

import co.edu.uco.easy.victusresidencias.victus_api.entity.CiudadEntity;
import co.edu.uco.easy.victusresidencias.victus_api.repository.CiudadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CiudadService {
    @Autowired
    private CiudadRepository repository;

    public List<CiudadEntity> findAll() {
        return repository.findAll();
    }

    public Optional<CiudadEntity> findById(UUID id) {
        return repository.findById(id);
    }

    public CiudadEntity save(CiudadEntity ciudad) {
        return repository.save(ciudad);
    }

    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    public boolean existsByNombre(String nombre) {
        return repository.existsByNombre(nombre);
    }

    public boolean existsByNombreAndDepartamentoId(String nombre, UUID departamentoId) {
        return repository.existsByNombreAndDepartamentoId(nombre, departamentoId);
    }
}