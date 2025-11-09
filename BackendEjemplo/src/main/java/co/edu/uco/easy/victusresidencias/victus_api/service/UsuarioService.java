package co.edu.uco.easy.victusresidencias.victus_api.service;

import co.edu.uco.easy.victusresidencias.victus_api.entity.UsuarioEntity;
import co.edu.uco.easy.victusresidencias.victus_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository repository;

    public List<UsuarioEntity> findAll() {
        return repository.findAll();
    }

    public Optional<UsuarioEntity> findById(UUID id) {
        return repository.findById(id);
    }

    public Optional<UsuarioEntity> findByCorreoElectronico(String correoElectronico) {
        return repository.findByCorreoElectronico(correoElectronico);
    }

    public UsuarioEntity save(UsuarioEntity usuario) {
        return repository.save(usuario);
    }

    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    public boolean existsByCorreoElectronico(String correoElectronico) {
        return repository.existsByCorreoElectronico(correoElectronico);
    }

    public boolean existsByNumeroIdentificacionAndTipoIdentificacionId(String numeroIdentificacion, UUID tipoIdentificacionId) {
        return repository.existsByNumeroIdentificacionAndTipoIdentificacionId(numeroIdentificacion, tipoIdentificacionId);
    }
}