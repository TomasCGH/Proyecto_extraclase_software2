package co.edu.uco.easy.victusresidencias.victus_api.dao.impl.postgresql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.DataVictusResidenciasException;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.TextHelper;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.UUIDHelper;
import co.edu.uco.easy.victusresidencias.victus_api.dao.DepartamentoDAO;
import co.edu.uco.easy.victusresidencias.victus_api.dao.impl.sql.SqlDAO;
import co.edu.uco.easy.victusresidencias.victus_api.entity.DepartamentoEntity;
import co.edu.uco.easy.victusresidencias.victus_api.entity.PaisEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

final class DepartamentoPostgreSQLDAO extends SqlDAO implements DepartamentoDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(DepartamentoPostgreSQLDAO.class);
    private static final String SQL_FROM = "FROM departamento ";
    private static final String SQL_SELECT = "SELECT d.id, d.nombre, d.pais_id, p.nombre as pais_nombre ";
    private static final String SQL_JOIN = "JOIN pais p ON p.id = d.pais_id ";
    private static final String SQL_DELETE = "DELETE FROM departamento WHERE id = ?";
    private static final String SQL_UPDATE = "UPDATE departamento SET nombre = ?, pais_id = ? WHERE id = ?";
    private static final String CLASS_NAME_SINGULAR = "Departamento";
    private static final String CLASS_NAME_PLURAL = "Departamentos";
    private static final String SQL_CREATE = "INSERT INTO departamento(id, nombre, pais_id) VALUES(?, ?, ?)";

    public DepartamentoPostgreSQLDAO(Connection connection) {
        super(connection);
    }

    @Override
    public DepartamentoEntity fingByID(UUID id) {
        var departamentoEntityFilter = new DepartamentoEntity();
        departamentoEntityFilter.setId(id);
        
        var result = findByFilter(departamentoEntityFilter);
        return (result.isEmpty()) ? null : result.get(0);
    }

    @Override
    public List<DepartamentoEntity> findAll() {
        return findByFilter(new DepartamentoEntity());
    }

    @Override
    public List<DepartamentoEntity> findByFilter(DepartamentoEntity filter) {
        final var statement = new StringBuilder();
        final var parameters = new ArrayList<>();
        final var resultSelect = new ArrayList<DepartamentoEntity>();
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
                var departamentoEntityTmp = new DepartamentoEntity();
                departamentoEntityTmp.setId(UUID.fromString(result.getString("id")));
                LOGGER.debug("Departamento ID added to list: {}", UUID.fromString(result.getString("id")));
                departamentoEntityTmp.setNombre(result.getString("nombre"));
                
                var paisEntity = new PaisEntity();
                paisEntity.setId(UUID.fromString(result.getString("pais_id")));
                paisEntity.setNombre(result.getString("pais_nombre"));
                departamentoEntityTmp.setPais(paisEntity);
                
                resultSelect.add(departamentoEntityTmp);        
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
        statement.append(SQL_FROM + "d ");
    }

    private void createJoin(final StringBuilder statement) {
        statement.append(SQL_JOIN);
    }

    private void createWhere(final StringBuilder statement, 
            final DepartamentoEntity filter, 
            final List<Object> parameters) {
        var whereClauseAdded = false;

        if (!UUIDHelper.isDefault(filter.getId())) {
            LOGGER.debug("Prepared statement with WHERE clause for ID: {}", filter.getId());
            statement.append("WHERE d.id = ? ");
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
            statement.append("d.nombre = ? ");
            parameters.add(filter.getNombre());
        }

        if (filter.getPais() != null && !UUIDHelper.isDefault(filter.getPais().getId())) {
            if (whereClauseAdded) {
                statement.append("AND ");
            } else {
                statement.append("WHERE ");
            }
            statement.append("d.pais_id = ? ");
            parameters.add(filter.getPais().getId());
        }
    }

    private void createOrderBy(final StringBuilder statement) {
        statement.append("ORDER BY d.nombre ASC");
    }

    @Override
    public void create(DepartamentoEntity data) {
        if (data.getPais() == null || UUIDHelper.isDefault(data.getPais().getId())) {
            throw DataVictusResidenciasException.crear(
                "El país es requerido para crear el departamento",
                "Se intentó crear un departamento sin especificar el país");
        }

        DepartamentoEntity filter = new DepartamentoEntity();
        filter.setNombre(data.getNombre());
        filter.setPais(data.getPais());
        
        if (!findByFilter(filter).isEmpty()) {
            throw DataVictusResidenciasException.crear(
                    String.format("El %s ya existe en el país especificado", CLASS_NAME_SINGULAR),
                    String.format("No se puede crear un %s con el nombre duplicado en el mismo país: ", CLASS_NAME_SINGULAR) + data.getNombre());
        }
        
        final StringBuilder statement = new StringBuilder();
        statement.append(SQL_CREATE);

        if (UUIDHelper.isDefault(data.getId())) {
            data.setId(UUIDHelper.generate());
        }

        try (final var preparedStatement = getConnection().prepareStatement(statement.toString())) {
            preparedStatement.setObject(1, data.getId());
            preparedStatement.setString(2, data.getNombre());
            preparedStatement.setObject(3, data.getPais().getId());

            preparedStatement.executeUpdate();
            LOGGER.info("Departamento created successfully with name: {}", data.getNombre());

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
    public void update(DepartamentoEntity data) {
        if (data.getPais() == null || UUIDHelper.isDefault(data.getPais().getId())) {
            throw DataVictusResidenciasException.crear(
                "El país es requerido para actualizar el departamento",
                "Se intentó actualizar un departamento sin especificar el país");
        }

        final StringBuilder statement = new StringBuilder();
        statement.append(SQL_UPDATE);

        try (final var preparedStatement = getConnection().prepareStatement(statement.toString())) {
            preparedStatement.setString(1, data.getNombre());
            preparedStatement.setObject(2, data.getPais().getId());
            preparedStatement.setObject(3, data.getId());

            preparedStatement.executeUpdate();

        } catch (final SQLException exception) {
            var userMessage = String.format("Se ha presentado un problema tratando de actualizar la información del %s. Por favor intente de nuevo y si el problema persiste reporte la novedad...",CLASS_NAME_SINGULAR);
            var technicalMessage = String.format("Se ha presentado un problema al tratar de actualizar la información del %s en la base de datos PostgreSQL. Por favor valide el log de errores para encontrar mayores detalles del problema presentado...",CLASS_NAME_SINGULAR);

            throw DataVictusResidenciasException.crear(userMessage, technicalMessage, exception);
        }
    }
}