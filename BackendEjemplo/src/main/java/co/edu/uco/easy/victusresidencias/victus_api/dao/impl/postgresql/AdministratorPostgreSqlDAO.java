package co.edu.uco.easy.victusresidencias.victus_api.dao.impl.postgresql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.DataVictusResidenciasException;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.TextHelper;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.UUIDHelper;
import co.edu.uco.easy.victusresidencias.victus_api.dao.AdministratorDAO;
import co.edu.uco.easy.victusresidencias.victus_api.dao.impl.sql.SqlDAO;
import co.edu.uco.easy.victusresidencias.victus_api.entity.AdministratorEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

final class AdministratorPostgreSQLDAO extends SqlDAO implements AdministratorDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdministratorPostgreSQLDAO.class);
	private static final String SQL_FROM = "FROM administrador ";
	private static final String SQL_SELECT = "SELECT id, nombre, apellido, tipo_documento, numero_documento, numero_contacto, email, contraseña ";
	private static final String SQL_DELETE = "DELETE FROM administrador WHERE id = ?";
	private static final String SQL_UPDATE = "UPDATE administrador SET nombre = ?, apellido = ?, tipo_documento = ?, numero_documento = ?, numero_contacto = ?, email = ?, contraseña = ? WHERE id = ?";
	private static final String CLASS_NAME_SINGULAR = "Administrador";
	private static final String CLASS_NAME_PLURAL = "Administradores";
	private static final String SQL_CREATE = "INSERT INTO administrador(id, nombre, apellido, tipo_documento, numero_documento, numero_contacto, email, contraseña) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
	
	public AdministratorPostgreSQLDAO(Connection connection) {
		super(connection);
	}	
	
	@Override
	public AdministratorEntity fingByID(UUID id) {
		var administratorEntityFilter = new AdministratorEntity();
	    administratorEntityFilter.setId(id);
	    
	    var result = findByFilter(administratorEntityFilter);
	    return (result.isEmpty()) ? null : result.get(0);
	}
	
	@Override
	public List<AdministratorEntity> findAll() {
		return findByFilter(new AdministratorEntity());
	}
	
	@Override
	public List<AdministratorEntity> findByFilter(AdministratorEntity filter) {
		final var statement = new StringBuilder();
	    final var parameters = new ArrayList<>();
	    final var resultSelect = new ArrayList<AdministratorEntity>();
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
	      //SELECT id, name, last_name, id_type, id_number, contact_number, email, password
	        while (result.next()) {
	            var administratorEntityTmp = new AdministratorEntity();
	            administratorEntityTmp.setId(UUID.fromString(result.getString("id")));
	            LOGGER.debug("Administrator ID added to list: {}", UUID.fromString(result.getString("id")));
	            administratorEntityTmp.setName(result.getString("nombre"));
	            administratorEntityTmp.setLastName(result.getString("apellido"));
	            administratorEntityTmp.setIdType(result.getString("tipo_documento"));
	            administratorEntityTmp.setIdNumber(result.getString("numero_documento"));
	            administratorEntityTmp.setContactNumber(result.getString("numero_contacto"));
	            administratorEntityTmp.setEmail(result.getString("email"));
	            administratorEntityTmp.setPassword(result.getString("contraseña"));
	            
	            resultSelect.add(administratorEntityTmp);		
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
	
	private void createSelect(final StringBuilder statement) {statement.append(SQL_SELECT);}
	
	private void createFrom(final StringBuilder statement) {
		statement.append(SQL_FROM);
	}

	private void createWhere(final StringBuilder statement, 
            final AdministratorEntity filter, 
            final List<Object> parameters) {
			if (!UUIDHelper.isDefault(filter.getId())) {
				LOGGER.debug("Prepared statement with WHERE clause for ID: {}", filter.getId());
				statement.append("WHERE id = ? ");
				parameters.add(filter.getId());
			} else if (!TextHelper.isEmpty(filter.getName())) {
				statement.append("WHERE nombre = ? ");
				parameters.add(filter.getName());
			} else if (!TextHelper.isEmpty(filter.getEmail())) {
				LOGGER.debug("Prepared statement with WHERE clause for email: {}", filter.getEmail());
				statement.append("WHERE email = ? ");
				parameters.add(filter.getEmail());
			}
	}
	
	private void createOrderBy(final StringBuilder statement) {
		statement.append("ORDER BY nombre ASC");
	}

	@Override
	public void create(AdministratorEntity data) {
	    AdministratorEntity filter = new AdministratorEntity();
	    AdministratorEntity filterEmail = new AdministratorEntity();
	    filter.setName(data.getName());
	    filterEmail.setEmail(data.getEmail());
	    if (!findByFilter(filter).isEmpty()) {
			throw DataVictusResidenciasException.crear(
					String.format("El %s ya existe",CLASS_NAME_SINGULAR),
					String.format("No se puede crear un %s con el nombre duplicado: ",CLASS_NAME_SINGULAR) + data.getName());
	    }else if(!findByFilter(filterEmail).isEmpty()) {
	        throw DataVictusResidenciasException.crear(
		            String.format("El email del %s ya existe",CLASS_NAME_SINGULAR),
					String.format("No se puede crear un %s con el email duplicado: ",CLASS_NAME_SINGULAR) + data.getEmail() );
	        		
		}
	    
	    final StringBuilder statement = new StringBuilder();
	    statement.append(SQL_CREATE);

	    if (UUIDHelper.isDefault(data.getId())) {
	        data.setId(UUIDHelper.generate());
	    }

	    try (final var preparedStatement = getConnection().prepareStatement(statement.toString())) {
	        preparedStatement.setObject(1, data.getId());
	        preparedStatement.setString(2, data.getName());
	        preparedStatement.setString(3, data.getLastName());
	        preparedStatement.setString(4, data.getIdType());
	        preparedStatement.setString(5, data.getIdNumber());
	        preparedStatement.setString(6, data.getContactNumber());
	        preparedStatement.setString(7, data.getEmail());
	        preparedStatement.setString(8, data.getPassword());

	        preparedStatement.executeUpdate();
	        LOGGER.info("Administrator created successfully with name: {}", data.getName());

	    } catch (final SQLException exception) {
			var userMessage = String.format("Se ha presentado un problema tratando de llevar a cabo el registro de la información del nuevo %s. Por favor intente de nuevo y si el problema persiste reporte la novedad...",CLASS_NAME_SINGULAR);
			var technicalMessage = String.format("Se ha presentado un problema al tratar de registrar la información del nuevo %s en la base de datos postgreSQL. Por favor valide el log de errores para encontrar mayores detalles del problema presentado...",CLASS_NAME_SINGULAR);

			throw DataVictusResidenciasException.crear(userMessage, technicalMessage, exception);
		}
	}

	@Override
	public void delete(UUID data) {
	    final StringBuilder statement = new StringBuilder();
	    statement.append(SQL_DELETE);	    try (final var preparedStatement = getConnection().prepareStatement(statement.toString())) {
	        preparedStatement.setObject(1, data);
	        preparedStatement.executeUpdate();

	    } catch (final SQLException exception) {
			var userMessage = String.format("Se ha presentado un problema tratando de eliminar el %s seleccionado. Por favor intente de nuevo y si el problema persiste reporte la novedad...",CLASS_NAME_SINGULAR);
			var technicalMessage = String.format("Se ha presentado un problema al tratar de eliminar el %s en la base de datos PostgreSQL. Por favor valide el log de errores para encontrar mayores detalles del problema presentado...",CLASS_NAME_SINGULAR);
			throw DataVictusResidenciasException.crear(userMessage, technicalMessage, exception);
		}
	}

	@Override
	public void update(AdministratorEntity data) {
		final StringBuilder statement = new StringBuilder();
	    statement.append(SQL_UPDATE);

	    try (final var preparedStatement = getConnection().prepareStatement(statement.toString())) {
	        preparedStatement.setString(1, data.getName());
	        preparedStatement.setString(2, data.getLastName());
	        preparedStatement.setString(3, data.getIdType());
	        preparedStatement.setString(4, data.getIdNumber());
	        preparedStatement.setString(5, data.getContactNumber());
	        preparedStatement.setString(6, data.getEmail());
	        preparedStatement.setString(7, data.getPassword());
	        preparedStatement.setObject(8, data.getId());

	        preparedStatement.executeUpdate();

	    } catch (final SQLException exception) {
			var userMessage = String.format("Se ha presentado un problema tratando de actualizar la información del %s. Por favor intente de nuevo y si el problema persiste reporte la novedad...",CLASS_NAME_SINGULAR);
			var technicalMessage = String.format("Se ha presentado un problema al tratar de actualizar la información del %s en la base de datos PostgreSQL. Por favor valide el log de errores para encontrar mayores detalles del problema presentado...",CLASS_NAME_SINGULAR);

			throw DataVictusResidenciasException.crear(userMessage, technicalMessage, exception);
		}
	}
}
