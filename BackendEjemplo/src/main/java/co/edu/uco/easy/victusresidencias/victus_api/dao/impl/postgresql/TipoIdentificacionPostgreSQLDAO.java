package co.edu.uco.easy.victusresidencias.victus_api.dao.impl.postgresql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.DataVictusResidenciasException;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.TextHelper;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.UUIDHelper;
import co.edu.uco.easy.victusresidencias.victus_api.dao.TipoIdentificacionDAO;
import co.edu.uco.easy.victusresidencias.victus_api.dao.impl.sql.SqlDAO;
import co.edu.uco.easy.victusresidencias.victus_api.entity.TipoIdentificacionEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

final class TipoIdentificacionPostgreSQLDAO extends SqlDAO implements TipoIdentificacionDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(TipoIdentificacionPostgreSQLDAO.class);
    private static final String SQL_FROM = "FROM tipo_identificacion ";
    private static final String SQL_SELECT = "SELECT DISTINCT ti.id, ti.nombre, ti.codigo ";
    private static final String SQL_DELETE = "DELETE FROM tipo_identificacion WHERE id = ?";
    private static final String SQL_UPDATE = "UPDATE tipo_identificacion SET nombre = ?, codigo = ? WHERE id = ?";
    private static final String CLASS_NAME_SINGULAR = "Tipo de Identificación";
    private static final String CLASS_NAME_PLURAL = "Tipos de Identificación";
    private static final String SQL_CREATE = "INSERT INTO tipo_identificacion(id, nombre, codigo) VALUES(?, ?, ?)";

    public TipoIdentificacionPostgreSQLDAO(Connection connection) {
        super(connection);
    }

    @Override
    public TipoIdentificacionEntity findByID(UUID id) {
        var tipoIdentificacionEntityFilter = new TipoIdentificacionEntity();
        tipoIdentificacionEntityFilter.setId(id);
        
        var result = findByFilter(tipoIdentificacionEntityFilter);
        return (result.isEmpty()) ? null : result.get(0);
    }

    @Override
    public List<TipoIdentificacionEntity> findAll() {
        return findByFilter(new TipoIdentificacionEntity());
    }

    @Override
    public List<TipoIdentificacionEntity> findByFilter(TipoIdentificacionEntity filter) {
        final var statement = new StringBuilder();
        final var parameters = new ArrayList<>();
        final var resultSelect = new ArrayList<TipoIdentificacionEntity>();
        var statementWasPrepared = false;

        createSelect(statement);
        createFrom(statement);
        createWhere(statement, filter, parameters);
        createOrderBy(statement);

        try (var preparedStatement = getConnection().prepareStatement(statement.toString())) {
            for (var arrayIndex = 0; arrayIndex < parameters.size(); arrayIndex++) {
                var statementIndex = arrayIndex + 1;
                preparedStatement.setObject(statementIndex, parameters.get(arrayIndex));
            }
            LOGGER.debug("SQL statement prepared: {}", statement);
            statementWasPrepared = true;
            final var result = preparedStatement.executeQuery();

            while (result.next()) {
                var tipoIdentificacionEntityTmp = new TipoIdentificacionEntity();
                tipoIdentificacionEntityTmp.setId(UUID.fromString(result.getString("id")));
                tipoIdentificacionEntityTmp.setNombre(result.getString("nombre"));
                tipoIdentificacionEntityTmp.setCodigo(result.getString("codigo"));
                
                resultSelect.add(tipoIdentificacionEntityTmp);        
            }
        } catch (final SQLException exception) {
            var userMessage = String.format("Se ha presentado un problema tratando de llevar a cabo la consulta de los %s.",CLASS_NAME_PLURAL);
            var technicalMessage = statementWasPrepared ?
                    String.format("Problema ejecutando la consulta de los %s en la base de datos.",CLASS_NAME_PLURAL) :
                    String.format("Problema preparando la consulta de los %s en la base de datos.",CLASS_NAME_PLURAL);

            throw DataVictusResidenciasException.crear(userMessage, technicalMessage, exception);
        }
        
        return resultSelect;
    }

    private void createSelect(final StringBuilder statement) {
        statement.append(SQL_SELECT);
    }

    private void createFrom(final StringBuilder statement) {
        statement.append(SQL_FROM + "ti ");
    }

    private void createWhere(final StringBuilder statement, 
            final TipoIdentificacionEntity filter, 
            final List<Object> parameters) {
        var whereClauseAdded = false;

        if (!UUIDHelper.isDefault(filter.getId())) {
            statement.append("WHERE id = ? ");
            parameters.add(filter.getId());
            whereClauseAdded = true;
        }

        if (!TextHelper.isEmpty(filter.getNombre())) {
            if (whereClauseAdded) {
                statement.append("AND ");
            } else {
                statement.append("WHERE ");
                whereClauseAdded = true;
            }
            statement.append("nombre = ? ");
            parameters.add(filter.getNombre());
        }

        if (!TextHelper.isEmpty(filter.getCodigo())) {
            if (whereClauseAdded) {
                statement.append("AND ");
            } else {
                statement.append("WHERE ");
            }
            statement.append("codigo = ? ");
            parameters.add(filter.getCodigo());
        }
    }

    private void createOrderBy(final StringBuilder statement) {
        statement.append("ORDER BY nombre ASC");
    }

    private void validateMandatoryData(TipoIdentificacionEntity data) {
        if (TextHelper.isEmpty(data.getNombre())) {
            throw DataVictusResidenciasException.crear(
                "El nombre es requerido para el tipo de identificación",
                "Se intentó crear/actualizar un tipo de identificación sin especificar el nombre");
        }

        if (TextHelper.isEmpty(data.getCodigo())) {
            throw DataVictusResidenciasException.crear(
                "El código es requerido para el tipo de identificación",
                "Se intentó crear/actualizar un tipo de identificación sin especificar el código");
        }
    }

    @Override
    public void create(TipoIdentificacionEntity data) {
        validateMandatoryData(data);

        TipoIdentificacionEntity filter = new TipoIdentificacionEntity();
        filter.setCodigo(data.getCodigo());
        
        if (!findByFilter(filter).isEmpty()) {
            throw DataVictusResidenciasException.crear(
                    String.format("El %s ya existe con el código especificado", CLASS_NAME_SINGULAR),
                    String.format("No se puede crear un %s con el código duplicado: ", CLASS_NAME_SINGULAR) + data.getCodigo());
        }
        
        final StringBuilder statement = new StringBuilder();
        statement.append(SQL_CREATE);

        if (UUIDHelper.isDefault(data.getId())) {
            data.setId(UUIDHelper.generate());
        }

        try (final var preparedStatement = getConnection().prepareStatement(statement.toString())) {
            preparedStatement.setObject(1, data.getId());
            preparedStatement.setString(2, data.getNombre());
            preparedStatement.setString(3, data.getCodigo());

            preparedStatement.executeUpdate();
            LOGGER.info("TipoIdentificacion created successfully with code: {}", data.getCodigo());

        } catch (final SQLException exception) {
            var userMessage = String.format("Se ha presentado un problema tratando de llevar a cabo el registro de la información del nuevo %s. Por favor intente de nuevo y si el problema persiste reporte la novedad...",CLASS_NAME_SINGULAR);
            var technicalMessage = String.format("Se ha presentado un problema al tratar de registrar la información del nuevo %s en la base de datos PostgreSQL. Por favor valide el log de errores para encontrar mayores detalles del problema presentado...",CLASS_NAME_SINGULAR);

            throw DataVictusResidenciasException.crear(userMessage, technicalMessage, exception);
        }
    }

    @Override
    public void delete(UUID data) {
        final StringBuilder statement = new StringBuilder();
        statement.append(SQL_DELETE);
        try (final var preparedStatement = getConnection().prepareStatement(statement.toString())) {
            preparedStatement.setObject(1, data);
            preparedStatement.executeUpdate();

        } catch (final SQLException exception) {
            var userMessage = String.format("Se ha presentado un problema tratando de eliminar el %s seleccionado. Por favor intente de nuevo y si el problema persiste reporte la novedad...",CLASS_NAME_SINGULAR);
            var technicalMessage = String.format("Se ha presentado un problema al tratar de eliminar el %s en la base de datos PostgreSQL. Por favor valide el log de errores para encontrar mayores detalles del problema presentado...",CLASS_NAME_SINGULAR);
            throw DataVictusResidenciasException.crear(userMessage, technicalMessage, exception);
        }
    }

    @Override
    public void update(TipoIdentificacionEntity data) {
        if (TextHelper.isEmpty(data.getCodigo())) {
            throw DataVictusResidenciasException.crear(
                "El código es requerido para actualizar el tipo de identificación",
                "Se intentó actualizar un tipo de identificación sin especificar el código");
        }

        TipoIdentificacionEntity filter = new TipoIdentificacionEntity();
        filter.setCodigo(data.getCodigo());
        var existingEntities = findByFilter(filter);
        
        if (!existingEntities.isEmpty() && !existingEntities.get(0).getId().equals(data.getId())) {
            throw DataVictusResidenciasException.crear(
                    String.format("El código ya está en uso por otro %s", CLASS_NAME_SINGULAR),
                    String.format("No se puede actualizar el %s con un código duplicado: ", CLASS_NAME_SINGULAR) + data.getCodigo());
        }

        final StringBuilder statement = new StringBuilder();
        statement.append(SQL_UPDATE);

        try (final var preparedStatement = getConnection().prepareStatement(statement.toString())) {
            preparedStatement.setString(1, data.getNombre());
            preparedStatement.setString(2, data.getCodigo());
            preparedStatement.setObject(3, data.getId());

            preparedStatement.executeUpdate();

        } catch (final SQLException exception) {
            var userMessage = String.format("Se ha presentado un problema tratando de actualizar la información del %s. Por favor intente de nuevo y si el problema persiste reporte la novedad...",CLASS_NAME_SINGULAR);
            var technicalMessage = String.format("Se ha presentado un problema al tratar de actualizar la información del %s en la base de datos PostgreSQL. Por favor valide el log de errores para encontrar mayores detalles del problema presentado...",CLASS_NAME_SINGULAR);

            throw DataVictusResidenciasException.crear(userMessage, technicalMessage, exception);
        }
    }
}