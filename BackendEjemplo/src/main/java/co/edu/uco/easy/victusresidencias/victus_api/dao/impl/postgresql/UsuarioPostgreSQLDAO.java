package co.edu.uco.easy.victusresidencias.victus_api.dao.impl.postgresql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.DataVictusResidenciasException;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.TextHelper;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.UUIDHelper;
import co.edu.uco.easy.victusresidencias.victus_api.dao.UsuarioDAO;
import co.edu.uco.easy.victusresidencias.victus_api.dao.impl.sql.SqlDAO;
import co.edu.uco.easy.victusresidencias.victus_api.entity.CiudadEntity;
import co.edu.uco.easy.victusresidencias.victus_api.entity.DepartamentoEntity;
import co.edu.uco.easy.victusresidencias.victus_api.entity.PaisEntity;
import co.edu.uco.easy.victusresidencias.victus_api.entity.TipoIdentificacionEntity;
import co.edu.uco.easy.victusresidencias.victus_api.entity.UsuarioEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

final class UsuarioPostgreSQLDAO extends SqlDAO implements UsuarioDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioPostgreSQLDAO.class);
    private static final String SQL_FROM = "FROM usuario ";
    private static final String SQL_SELECT = "SELECT DISTINCT u.id, u.primer_nombre, u.segundo_nombre, " +
                                           "u.primer_apellido, u.segundo_apellido, u.correo_electronico, " +
                                           "u.numero_telefono_movil, u.numero_identificacion, " +
                                           "u.tipo_identificacion_id, ti.nombre as tipo_identificacion_nombre, " +
                                           "ti.codigo as tipo_identificacion_codigo, " +
                                           "u.ciudad_residencia_id, c.nombre as ciudad_nombre, " +
                                           "d.id as departamento_id, d.nombre as departamento_nombre, " +
                                           "p.id as pais_id, p.nombre as pais_nombre ";
    private static final String SQL_JOIN = "JOIN tipo_identificacion ti ON ti.id = u.tipo_identificacion_id " +
                                         "JOIN ciudad c ON c.id = u.ciudad_id " +
                                         "JOIN departamento d ON d.id = c.departamento_id " +
                                         "JOIN pais p ON p.id = d.pais_id ";
    private static final String SQL_DELETE = "DELETE FROM usuario WHERE id = ?";
    private static final String SQL_UPDATE = "UPDATE usuario SET nombre = ?, apellido = ?, correo = ?, " +
                                           "numero_identificacion = ?, tipo_identificacion_id = ?, ciudad_id = ? " +
                                           "WHERE id = ?";
    private static final String CLASS_NAME_SINGULAR = "Usuario";
    private static final String CLASS_NAME_PLURAL = "Usuarios";
    private static final String SQL_CREATE = "INSERT INTO usuario(id, nombre, apellido, correo, numero_identificacion, " +
                                           "tipo_identificacion_id, ciudad_id) VALUES(?, ?, ?, ?, ?, ?, ?)";

    public UsuarioPostgreSQLDAO(Connection connection) {
        super(connection);
    }

    @Override
    public UsuarioEntity findByID(UUID id) {
        var usuarioEntityFilter = new UsuarioEntity();
        usuarioEntityFilter.setId(id);
        
        var result = findByFilter(usuarioEntityFilter);
        return (result.isEmpty()) ? null : result.get(0);
    }

    @Override
    public List<UsuarioEntity> findAll() {
        return findByFilter(new UsuarioEntity());
    }

    @Override
    public List<UsuarioEntity> findByFilter(UsuarioEntity filter) {
        final var statement = new StringBuilder();
        final var parameters = new ArrayList<>();
        final var resultSelect = new ArrayList<UsuarioEntity>();
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
                var usuarioEntityTmp = new UsuarioEntity();
                usuarioEntityTmp.setId(UUID.fromString(result.getString("id")));
                usuarioEntityTmp.setPrimerNombre(result.getString("primer_nombre"));
                usuarioEntityTmp.setSegundoNombre(result.getString("segundo_nombre"));
                usuarioEntityTmp.setPrimerApellido(result.getString("primer_apellido"));
                usuarioEntityTmp.setSegundoApellido(result.getString("segundo_apellido"));
                usuarioEntityTmp.setCorreoElectronico(result.getString("correo_electronico"));
                usuarioEntityTmp.setNumeroTelefonoMovil(result.getString("numero_telefono_movil"));
                usuarioEntityTmp.setNumeroIdentificacion(result.getString("numero_identificacion"));
                
                var tipoIdentificacionEntity = new TipoIdentificacionEntity();
                tipoIdentificacionEntity.setId(UUID.fromString(result.getString("tipo_identificacion_id")));
                tipoIdentificacionEntity.setNombre(result.getString("tipo_identificacion_nombre"));
                tipoIdentificacionEntity.setCodigo(result.getString("tipo_identificacion_codigo"));
                
                var paisEntity = new PaisEntity();
                paisEntity.setId(UUID.fromString(result.getString("pais_id")));
                paisEntity.setNombre(result.getString("pais_nombre"));
                
                var departamentoEntity = new DepartamentoEntity();
                departamentoEntity.setId(UUID.fromString(result.getString("departamento_id")));
                departamentoEntity.setNombre(result.getString("departamento_nombre"));
                departamentoEntity.setPais(paisEntity);
                
                var ciudadEntity = new CiudadEntity();
                ciudadEntity.setId(UUID.fromString(result.getString("ciudad_residencia_id")));
                ciudadEntity.setNombre(result.getString("ciudad_nombre"));
                ciudadEntity.setDepartamento(departamentoEntity);
                
                usuarioEntityTmp.setTipoIdentificacion(tipoIdentificacionEntity);
                usuarioEntityTmp.setCiudadResidencia(ciudadEntity);
                
                resultSelect.add(usuarioEntityTmp);        
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
        statement.append(SQL_FROM + "u ");
    }

    private void createJoin(final StringBuilder statement) {
        statement.append(SQL_JOIN);
    }

    private void createWhere(final StringBuilder statement, 
            final UsuarioEntity filter, 
            final List<Object> parameters) {
        var whereClauseAdded = false;

        if (!UUIDHelper.isDefault(filter.getId())) {
            statement.append("WHERE u.id = ? ");
            parameters.add(filter.getId());
            whereClauseAdded = true;
        }

        if (!TextHelper.isEmpty(filter.getPrimerNombre())) {
            if (whereClauseAdded) {
                statement.append("AND ");
            } else {
                statement.append("WHERE ");
                whereClauseAdded = true;
            }
            statement.append("u.primer_nombre = ? ");
            parameters.add(filter.getPrimerNombre());
        }

        if (!TextHelper.isEmpty(filter.getPrimerApellido())) {
            if (whereClauseAdded) {
                statement.append("AND ");
            } else {
                statement.append("WHERE ");
                whereClauseAdded = true;
            }
            statement.append("u.primer_apellido = ? ");
            parameters.add(filter.getPrimerApellido());
        }

        if (!TextHelper.isEmpty(filter.getCorreoElectronico())) {
            if (whereClauseAdded) {
                statement.append("AND ");
            } else {
                statement.append("WHERE ");
                whereClauseAdded = true;
            }
            statement.append("u.correo_electronico = ? ");
            parameters.add(filter.getCorreoElectronico());
        }

        if (!TextHelper.isEmpty(filter.getNumeroIdentificacion())) {
            if (whereClauseAdded) {
                statement.append("AND ");
            } else {
                statement.append("WHERE ");
                whereClauseAdded = true;
            }
            statement.append("u.numero_identificacion = ? ");
            parameters.add(filter.getNumeroIdentificacion());
        }

        if (filter.getTipoIdentificacion() != null && !UUIDHelper.isDefault(filter.getTipoIdentificacion().getId())) {
            if (whereClauseAdded) {
                statement.append("AND ");
            } else {
                statement.append("WHERE ");
                whereClauseAdded = true;
            }
            statement.append("u.tipo_identificacion_id = ? ");
            parameters.add(filter.getTipoIdentificacion().getId());
        }

        if (filter.getCiudadResidencia() != null && !UUIDHelper.isDefault(filter.getCiudadResidencia().getId())) {
            if (whereClauseAdded) {
                statement.append("AND ");
            } else {
                statement.append("WHERE ");
            }
            statement.append("u.ciudad_residencia_id = ? ");
            parameters.add(filter.getCiudadResidencia().getId());
        }
    }

    private void createOrderBy(final StringBuilder statement) {
        statement.append("ORDER BY u.apellido, u.nombre ASC");
    }

    @Override
    public void create(UsuarioEntity data) {
        validateMandatoryData(data);

        // Validar duplicados de correo
        UsuarioEntity filterByEmail = new UsuarioEntity();
        filterByEmail.setCorreoElectronico(data.getCorreoElectronico());
        if (!findByFilter(filterByEmail).isEmpty()) {
            throw DataVictusResidenciasException.crear(
                    "Ya existe un usuario registrado con el correo especificado",
                    "Intento de crear usuario con correo duplicado: " + data.getCorreoElectronico());
        }

        // Validar duplicados de número de identificación
        UsuarioEntity filterByIdentification = new UsuarioEntity();
        filterByIdentification.setNumeroIdentificacion(data.getNumeroIdentificacion());
        if (!findByFilter(filterByIdentification).isEmpty()) {
            throw DataVictusResidenciasException.crear(
                    "Ya existe un usuario registrado con el número de identificación especificado",
                    "Intento de crear usuario con número de identificación duplicado: " + data.getNumeroIdentificacion());
        }
        
        final StringBuilder statement = new StringBuilder();
        statement.append(SQL_CREATE);

        if (UUIDHelper.isDefault(data.getId())) {
            data.setId(UUIDHelper.generate());
        }

        try (final var preparedStatement = getConnection().prepareStatement(statement.toString())) {
            preparedStatement.setObject(1, data.getId());
            preparedStatement.setString(2, data.getPrimerNombre());
            preparedStatement.setString(3, data.getSegundoNombre());
            preparedStatement.setString(4, data.getPrimerApellido());
            preparedStatement.setString(5, data.getSegundoApellido());
            preparedStatement.setString(6, data.getCorreoElectronico());
            preparedStatement.setString(7, data.getNumeroTelefonoMovil());
            preparedStatement.setString(8, data.getNumeroIdentificacion());
            preparedStatement.setObject(9, data.getTipoIdentificacion().getId());
            preparedStatement.setObject(10, data.getCiudadResidencia().getId());
            preparedStatement.setBoolean(11, data.isCorreoElectronicoConfirmado());
            preparedStatement.setBoolean(12, data.isNumeroTelefonoMovilConfirmado());

            preparedStatement.executeUpdate();
            LOGGER.info("Usuario created successfully with email: {}", data.getCorreoElectronico());

        } catch (final SQLException exception) {
            var userMessage = String.format("Se ha presentado un problema tratando de llevar a cabo el registro de la información del nuevo %s. Por favor intente de nuevo y si el problema persiste reporte la novedad...",CLASS_NAME_SINGULAR);
            var technicalMessage = String.format("Se ha presentado un problema al tratar de registrar la información del nuevo %s en la base de datos PostgreSQL. Por favor valide el log de errores para encontrar mayores detalles del problema presentado...",CLASS_NAME_SINGULAR);

            throw DataVictusResidenciasException.crear(userMessage, technicalMessage, exception);
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email != null && email.matches(emailRegex);
    }

    private void validateMandatoryData(UsuarioEntity data) {
        if (TextHelper.isEmpty(data.getPrimerNombre())) {
            throw DataVictusResidenciasException.crear(
                "El primer nombre es requerido para el usuario",
                "Se intentó crear/actualizar un usuario sin especificar el primer nombre");
        }

        if (TextHelper.isEmpty(data.getPrimerApellido())) {
            throw DataVictusResidenciasException.crear(
                "El primer apellido es requerido para el usuario",
                "Se intentó crear/actualizar un usuario sin especificar el primer apellido");
        }

        if (TextHelper.isEmpty(data.getCorreoElectronico())) {
            throw DataVictusResidenciasException.crear(
                "El correo electrónico es requerido para el usuario",
                "Se intentó crear/actualizar un usuario sin especificar el correo electrónico");
        }

        if (!isValidEmail(data.getCorreoElectronico())) {
            throw DataVictusResidenciasException.crear(
                "El formato del correo electrónico no es válido",
                "Se intentó crear/actualizar un usuario con un correo electrónico inválido: " + data.getCorreoElectronico());
        }

        if (TextHelper.isEmpty(data.getNumeroIdentificacion())) {
            throw DataVictusResidenciasException.crear(
                "El número de identificación es requerido para el usuario",
                "Se intentó crear/actualizar un usuario sin especificar el número de identificación");
        }

        if (data.getTipoIdentificacion() == null || UUIDHelper.isDefault(data.getTipoIdentificacion().getId())) {
            throw DataVictusResidenciasException.crear(
                "El tipo de identificación es requerido para el usuario",
                "Se intentó crear/actualizar un usuario sin especificar el tipo de identificación");
        }

        if (data.getCiudadResidencia() == null || UUIDHelper.isDefault(data.getCiudadResidencia().getId())) {
            throw DataVictusResidenciasException.crear(
                "La ciudad de residencia es requerida para el usuario",
                "Se intentó crear/actualizar un usuario sin especificar la ciudad de residencia");
        }

        if (TextHelper.isEmpty(data.getNumeroTelefonoMovil())) {
            throw DataVictusResidenciasException.crear(
                "El número de teléfono móvil es requerido para el usuario",
                "Se intentó crear/actualizar un usuario sin especificar el número de teléfono móvil");
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
    public void update(UsuarioEntity data) {
        validateMandatoryData(data);

        // Validar duplicados de correo
        UsuarioEntity filterByEmail = new UsuarioEntity();
        filterByEmail.setCorreoElectronico(data.getCorreoElectronico());
        var existingUsersWithEmail = findByFilter(filterByEmail);
        if (!existingUsersWithEmail.isEmpty() && !existingUsersWithEmail.get(0).getId().equals(data.getId())) {
            throw DataVictusResidenciasException.crear(
                    "El correo especificado ya está registrado para otro usuario",
                    "Intento de actualizar usuario con correo duplicado: " + data.getCorreoElectronico());
        }

        // Validar duplicados de número de identificación
        UsuarioEntity filterByIdentification = new UsuarioEntity();
        filterByIdentification.setNumeroIdentificacion(data.getNumeroIdentificacion());
        var existingUsersWithId = findByFilter(filterByIdentification);
        if (!existingUsersWithId.isEmpty() && !existingUsersWithId.get(0).getId().equals(data.getId())) {
            throw DataVictusResidenciasException.crear(
                    "El número de identificación especificado ya está registrado para otro usuario",
                    "Intento de actualizar usuario con número de identificación duplicado: " + data.getNumeroIdentificacion());
        }

        final StringBuilder statement = new StringBuilder();
        statement.append(SQL_UPDATE);

        try (final var preparedStatement = getConnection().prepareStatement(statement.toString())) {
            preparedStatement.setString(1, data.getPrimerNombre());
            preparedStatement.setString(2, data.getSegundoNombre());
            preparedStatement.setString(3, data.getPrimerApellido());
            preparedStatement.setString(4, data.getSegundoApellido());
            preparedStatement.setString(5, data.getCorreoElectronico());
            preparedStatement.setString(6, data.getNumeroTelefonoMovil());
            preparedStatement.setString(7, data.getNumeroIdentificacion());
            preparedStatement.setObject(8, data.getTipoIdentificacion().getId());
            preparedStatement.setObject(9, data.getCiudadResidencia().getId());
            preparedStatement.setBoolean(10, data.isCorreoElectronicoConfirmado());
            preparedStatement.setBoolean(11, data.isNumeroTelefonoMovilConfirmado());
            preparedStatement.setObject(12, data.getId());

            preparedStatement.executeUpdate();

        } catch (final SQLException exception) {
            var userMessage = String.format("Se ha presentado un problema tratando de actualizar la información del %s. Por favor intente de nuevo y si el problema persiste reporte la novedad...",CLASS_NAME_SINGULAR);
            var technicalMessage = String.format("Se ha presentado un problema al tratar de actualizar la información del %s en la base de datos PostgreSQL. Por favor valide el log de errores para encontrar mayores detalles del problema presentado...",CLASS_NAME_SINGULAR);

            throw DataVictusResidenciasException.crear(userMessage, technicalMessage, exception);
        }
    }
}