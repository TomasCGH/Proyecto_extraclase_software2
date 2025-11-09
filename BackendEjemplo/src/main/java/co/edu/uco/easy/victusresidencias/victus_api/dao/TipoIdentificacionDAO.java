package co.edu.uco.easy.victusresidencias.victus_api.dao;

import java.util.List;
import java.util.UUID;

import co.edu.uco.easy.victusresidencias.victus_api.entity.TipoIdentificacionEntity;

public interface TipoIdentificacionDAO {
    TipoIdentificacionEntity findByID(UUID id);
    List<TipoIdentificacionEntity> findAll();
    List<TipoIdentificacionEntity> findByFilter(TipoIdentificacionEntity filter);
    void create(TipoIdentificacionEntity data);
    void update(TipoIdentificacionEntity data);
    void delete(UUID data);
}