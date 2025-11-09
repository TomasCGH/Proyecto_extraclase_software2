package co.edu.uco.easy.victusresidencias.victus_api.dao;

import java.util.UUID;
import co.edu.uco.easy.victusresidencias.victus_api.entity.CiudadEntity;

public interface CiudadDAO 
    extends RetrieveDAO<CiudadEntity, UUID>,
            CreateDAO<CiudadEntity>,
            DeleteDAO<UUID>,
            UpdateDAO<CiudadEntity> {
}