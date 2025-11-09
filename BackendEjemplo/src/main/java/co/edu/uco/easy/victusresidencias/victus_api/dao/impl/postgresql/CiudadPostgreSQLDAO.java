package co.edu.uco.easy.victusresidencias.victus_api.dao.impl.postgresql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.DataVictusResidenciasException;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.TextHelper;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.UUIDHelper;
import co.edu.uco.easy.victusresidencias.victus_api.dao.CiudadDAO;
import co.edu.uco.easy.victusresidencias.victus_api.dao.impl.sql.SqlDAO;
import co.edu.uco.easy.victusresidencias.victus_api.entity.CiudadEntity;
import co.edu.uco.easy.victusresidencias.victus_api.entity.DepartamentoEntity;
import co.edu.uco.easy.victusresidencias.victus_api.entity.PaisEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

final class CiudadPostgreSQLDAO extends SqlDAO implements CiudadDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(CiudadPostgreSQLDAO.class);
    private static final String SQL_FROM = "FROM ciudad ";
    private static final String SQL_SELECT = "SELECT c.id, c.nombre, c.departamento_id, " +
                                           "d.nombre as departamento_nombre, d.pais_id, " +
                                           "p.nombre as pais_nombre ";
    private static final String SQL_JOIN = "JOIN departamento d ON d.id = c.departamento_id " +
                                         "JOIN pais p ON p.id = d.pais_id ";
    private static final String SQL_DELETE = "DELETE FROM ciudad WHERE id = ?";
    private static final String SQL_UPDATE = "UPDATE ciudad SET nombre = ?, departamento_id = ? WHERE id = ?";
    private static final String CLASS_NAME_SINGULAR = "Ciudad";
    private static final String CLASS_NAME_PLURAL = "Ciudades";
    private static final String SQL_CREATE = "INSERT INTO ciudad(id, nombre, departamento_id) VALUES(?, ?, ?)";

    public CiudadPostgreSQLDAO(Connection connection) {
        super(connection);
    }

    @Override
    public CiudadEntity fingByID(UUID id) {
        var ciudadEntityFilter = new CiudadEntity();
        ciudadEntityFilter.setId(id);
        
        var result = findByFilter(ciudadEntityFilter);
        return (result.isEmpty()) ? null : result.get(0);
    }

    @Override
    public List<CiudadEntity> findAll() {
        return findByFilter(new CiudadEntity());
    }

    @Override
    public List<CiudadEntity> findByFilter(CiudadEntity filter) {
        final var statement = new StringBuilder();
        final var parameters = new ArrayList<>();
        final var resultSelect = new ArrayList<CiudadEntity>();
        var statementWasPrepared = false;

        createSelect(statement);
        createFrom(statement);
        createJoin(statement);
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
                var ciudadEntityTmp = new CiudadEntity();
                ciudadEntityTmp.setId(UUID.fromString(result.getString("id")));
                LOGGER.debug("Ciudad ID added to list: {}", UUID.fromString(result.getString("id")));
                ciudadEntityTmp.setNombre(result.getString("nombre"));
                
                var paisEntity = new PaisEntity();
                paisEntity.setId(UUID.fromString(result.getString("pais_id")));
                paisEntity.setNombre(result.getString("pais_nombre"));
                
                var departamentoEntity = new DepartamentoEntity();
                departamentoEntity.setId(UUID.fromString(result.getString("departamento_id")));
                departamentoEntity.setNombre(result.getString("departamento_nombre"));
                departamentoEntity.setPais(paisEntity);
                
                ciudadEntityTmp.setDepartamento(departamentoEntity);
                
                resultSelect.add(ciudadEntityTmp);        
            }
        } catch (final SQLException exception) {
            var userMessage = String.format("Se ha presentado un problema tratando de llevar a cabo la consulta de las %s.",CLASS_NAME_PLURAL);
            var technicalMessage = statementWasPrepared ?
                    String.format("Problema ejecutando la consulta de las %s en la base de datos.",CLASS_NAME_PLURAL) :
                    String.format("Problema preparando la consulta de las %s en la base de datos.",CLASS_NAME_PLURAL);

            throw DataVictusResidenciasException.crear(userMessage, technicalMessage, exception);
        }
        
        return resultSelect;
    }

    private void createSelect(final StringBuilder statement) {
        statement.append(SQL_SELECT);
    }

    private void createFrom(final StringBuilder statement) {
        statement.append(SQL_FROM + "c ");
    }

    private void createJoin(final StringBuilder statement) {
        statement.append(SQL_JOIN);
    }

    private void createWhere(final StringBuilder statement, 
            final CiudadEntity filter, 
            final List<Object> parameters) {
        var whereClauseAdded = false;

        if (!UUIDHelper.isDefault(filter.getId())) {
            LOGGER.debug("Prepared statement with WHERE clause for ID: {}", filter.getId());
            statement.append("WHERE c.id = ? ");
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
            statement.append("c.nombre = ? ");
            parameters.add(filter.getNombre());
        }

        if (filter.getDepartamento() != null && !UUIDHelper.isDefault(filter.getDepartamento().getId())) {
            if (whereClauseAdded) {
                statement.append("AND ");
            } else {
                statement.append("WHERE ");
            }
            statement.append("c.departamento_id = ? ");
            parameters.add(filter.getDepartamento().getId());
        }
    }

    private void createOrderBy(final StringBuilder statement) {
        statement.append("ORDER BY c.nombre ASC");
    }

    @Override
    public void create(CiudadEntity data) {
        if (data.getDepartamento() == null || UUIDHelper.isDefault(data.getDepartamento().getId())) {
            throw DataVictusResidenciasException.crear(
                "El departamento es requerido para crear la ciudad",
                "Se intentó crear una ciudad sin especificar el departamento");
        }

        CiudadEntity filter = new CiudadEntity();
        filter.setNombre(data.getNombre());
        filter.setDepartamento(data.getDepartamento());
        
        if (!findByFilter(filter).isEmpty()) {
            throw DataVictusResidenciasException.crear(
                    String.format("La %s ya existe en el departamento especificado", CLASS_NAME_SINGULAR),
                    String.format("No se puede crear una %s con el nombre duplicado en el mismo departamento: ", CLASS_NAME_SINGULAR) + data.getNombre());
        }
        
        final StringBuilder statement = new StringBuilder();
        statement.append(SQL_CREATE);

        if (UUIDHelper.isDefault(data.getId())) {
            data.setId(UUIDHelper.generate());
        }

        try (final var preparedStatement = getConnection().prepareStatement(statement.toString())) {
            preparedStatement.setObject(1, data.getId());
            preparedStatement.setString(2, data.getNombre());
            preparedStatement.setObject(3, data.getDepartamento().getId());

            preparedStatement.executeUpdate();
            LOGGER.info("Ciudad created successfully with name: {}", data.getNombre());

        } catch (final SQLException exception) {
            var userMessage = String.format("Se ha presentado un problema tratando de llevar a cabo el registro de la información de la nueva %s. Por favor intente de nuevo y si el problema persiste reporte la novedad...",CLASS_NAME_SINGULAR);
            var technicalMessage = String.format("Se ha presentado un problema al tratar de registrar la información de la nueva %s en la base de datos PostgreSQL. Por favor valide el log de errores para encontrar mayores detalles del problema presentado...",CLASS_NAME_SINGULAR);

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
            var userMessage = String.format("Se ha presentado un problema tratando de eliminar la %s seleccionada. Por favor intente de nuevo y si el problema persiste reporte la novedad...",CLASS_NAME_SINGULAR);
            var technicalMessage = String.format("Se ha presentado un problema al tratar de eliminar la %s en la base de datos PostgreSQL. Por favor valide el log de errores para encontrar mayores detalles del problema presentado...",CLASS_NAME_SINGULAR);
            throw DataVictusResidenciasException.crear(userMessage, technicalMessage, exception);
        }
    }

    @Override
    public void update(CiudadEntity data) {
        if (data.getDepartamento() == null || UUIDHelper.isDefault(data.getDepartamento().getId())) {
            throw DataVictusResidenciasException.crear(
                "El departamento es requerido para actualizar la ciudad",
                "Se intentó actualizar una ciudad sin especificar el departamento");
        }

        final StringBuilder statement = new StringBuilder();
        statement.append(SQL_UPDATE);

        try (final var preparedStatement = getConnection().prepareStatement(statement.toString())) {
            preparedStatement.setString(1, data.getNombre());
            preparedStatement.setObject(2, data.getDepartamento().getId());
            preparedStatement.setObject(3, data.getId());

            preparedStatement.executeUpdate();

        } catch (final SQLException exception) {
            var userMessage = String.format("Se ha presentado un problema tratando de actualizar la información de la %s. Por favor intente de nuevo y si el problema persiste reporte la novedad...",CLASS_NAME_SINGULAR);
            var technicalMessage = String.format("Se ha presentado un problema al tratar de actualizar la información de la %s en la base de datos PostgreSQL. Por favor valide el log de errores para encontrar mayores detalles del problema presentado...",CLASS_NAME_SINGULAR);

            throw DataVictusResidenciasException.crear(userMessage, technicalMessage, exception);
        }
    }
}