package co.edu.uco.easy.victusresidencias.victus_api.dao.impl.postgresql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.DataVictusResidenciasException;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.TextHelper;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.UUIDHelper;
import co.edu.uco.easy.victusresidencias.victus_api.dao.PaisDAO;
import co.edu.uco.easy.victusresidencias.victus_api.dao.impl.sql.SqlDAO;
import co.edu.uco.easy.victusresidencias.victus_api.entity.PaisEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

final class PaisPostgreSQLDAO extends SqlDAO implements PaisDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaisPostgreSQLDAO.class);
    private static final String SQL_FROM = "FROM pais ";
    private static final String SQL_SELECT = "SELECT id, nombre ";
    private static final String SQL_DELETE = "DELETE FROM pais WHERE id = ?";
    private static final String SQL_UPDATE = "UPDATE pais SET nombre = ? WHERE id = ?";
    private static final String CLASS_NAME_SINGULAR = "País";
    private static final String CLASS_NAME_PLURAL = "Países";
    private static final String SQL_CREATE = "INSERT INTO pais(id, nombre) VALUES(?, ?)";

    public PaisPostgreSQLDAO(Connection connection) {
        super(connection);
    }

    @Override
    public PaisEntity fingByID(UUID id) {
        var paisEntityFilter = new PaisEntity();
        paisEntityFilter.setId(id);
        
        var result = findByFilter(paisEntityFilter);
        return (result.isEmpty()) ? null : result.get(0);
    }

    @Override
    public List<PaisEntity> findAll() {
        return findByFilter(new PaisEntity());
    }

    @Override
    public List<PaisEntity> findByFilter(PaisEntity filter) {
        final var statement = new StringBuilder();
        final var parameters = new ArrayList<>();
        final var resultSelect = new ArrayList<PaisEntity>();
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
                var paisEntityTmp = new PaisEntity();
                paisEntityTmp.setId(UUID.fromString(result.getString("id")));
                LOGGER.debug("País ID added to list: {}", UUID.fromString(result.getString("id")));
                paisEntityTmp.setNombre(result.getString("nombre"));
                
                resultSelect.add(paisEntityTmp);        
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
        statement.append(SQL_FROM);
    }

    private void createWhere(final StringBuilder statement, 
            final PaisEntity filter, 
            final List<Object> parameters) {
        if (!UUIDHelper.isDefault(filter.getId())) {
            LOGGER.debug("Prepared statement with WHERE clause for ID: {}", filter.getId());
            statement.append("WHERE id = ? ");
            parameters.add(filter.getId());
        } else if (!TextHelper.isEmpty(filter.getNombre())) {
            statement.append("WHERE nombre = ? ");
            parameters.add(filter.getNombre());
        }
    }

    private void createOrderBy(final StringBuilder statement) {
        statement.append("ORDER BY nombre ASC");
    }

    @Override
    public void create(PaisEntity data) {
        PaisEntity filter = new PaisEntity();
        filter.setNombre(data.getNombre());
        if (!findByFilter(filter).isEmpty()) {
            throw DataVictusResidenciasException.crear(
                    String.format("El %s ya existe",CLASS_NAME_SINGULAR),
                    String.format("No se puede crear un %s con el nombre duplicado: ",CLASS_NAME_SINGULAR) + data.getNombre());
        }
        
        final StringBuilder statement = new StringBuilder();
        statement.append(SQL_CREATE);

        if (UUIDHelper.isDefault(data.getId())) {
            data.setId(UUIDHelper.generate());
        }

        try (final var preparedStatement = getConnection().prepareStatement(statement.toString())) {
            preparedStatement.setObject(1, data.getId());
            preparedStatement.setString(2, data.getNombre());

            preparedStatement.executeUpdate();
            LOGGER.info("País created successfully with name: {}", data.getNombre());

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
    public void update(PaisEntity data) {
        final StringBuilder statement = new StringBuilder();
        statement.append(SQL_UPDATE);

        try (final var preparedStatement = getConnection().prepareStatement(statement.toString())) {
            preparedStatement.setString(1, data.getNombre());
            preparedStatement.setObject(2, data.getId());

            preparedStatement.executeUpdate();

        } catch (final SQLException exception) {
            var userMessage = String.format("Se ha presentado un problema tratando de actualizar la información del %s. Por favor intente de nuevo y si el problema persiste reporte la novedad...",CLASS_NAME_SINGULAR);
            var technicalMessage = String.format("Se ha presentado un problema al tratar de actualizar la información del %s en la base de datos PostgreSQL. Por favor valide el log de errores para encontrar mayores detalles del problema presentado...",CLASS_NAME_SINGULAR);

            throw DataVictusResidenciasException.crear(userMessage, technicalMessage, exception);
        }
    }
}