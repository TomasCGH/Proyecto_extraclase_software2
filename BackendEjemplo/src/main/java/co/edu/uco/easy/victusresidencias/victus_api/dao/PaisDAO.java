package co.edu.uco.easy.victusresidencias.victus_api.dao;

import java.util.UUID;
import co.edu.uco.easy.victusresidencias.victus_api.entity.PaisEntity;

public interface PaisDAO 
    extends RetrieveDAO<PaisEntity, UUID>,
            CreateDAO<PaisEntity>,
            DeleteDAO<UUID>,
            UpdateDAO<PaisEntity> {
}