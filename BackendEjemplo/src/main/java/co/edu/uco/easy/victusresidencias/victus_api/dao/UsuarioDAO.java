package co.edu.uco.easy.victusresidencias.victus_api.dao;

import java.util.List;
import java.util.UUID;

import co.edu.uco.easy.victusresidencias.victus_api.entity.UsuarioEntity;

public interface UsuarioDAO {
    UsuarioEntity findByID(UUID id);
    List<UsuarioEntity> findAll();
    List<UsuarioEntity> findByFilter(UsuarioEntity filter);
    void create(UsuarioEntity data);
    void update(UsuarioEntity data);
    void delete(UUID data);
}