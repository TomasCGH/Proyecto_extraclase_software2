package co.edu.uco.easy.victusresidencias.victus_api.dao;

import java.util.UUID;
import co.edu.uco.easy.victusresidencias.victus_api.entity.DepartamentoEntity;

public interface DepartamentoDAO 
    extends RetrieveDAO<DepartamentoEntity, UUID>,
            CreateDAO<DepartamentoEntity>,
            DeleteDAO<UUID>,
            UpdateDAO<DepartamentoEntity> {
}