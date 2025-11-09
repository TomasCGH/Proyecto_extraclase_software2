package co.edu.uco.easy.victusresidencias.victus_api.dao.impl.postgresql;

import java.sql.Connection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.SqlConnectionHelper;
import co.edu.uco.easy.victusresidencias.victus_api.dao.*;

@Component
public final class PostgreSqlDAOFactory extends DAOFactory {

    private Connection connection;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password:NOT_FOUND}")
    private String password;

    public PostgreSqlDAOFactory(Environment environment) {
        super(environment);
    }

   

    @Override
    protected void openConnection() {
        SqlConnectionHelper.validateIfConnectionIsOpen(connection);
        connection = SqlConnectionHelper.openConnectionPostgreSQL(url, user, password);
        
    }

    @Override
    public void initTransaction() {
        SqlConnectionHelper.initTransaction(connection);
    }

    @Override
    public void commitTransaction() {
        SqlConnectionHelper.commitTransaction(connection);
    }

    @Override
    public void rollbackTransaction() {
        SqlConnectionHelper.rollbackTransaction(connection);
    }

    @Override
    public void closeConnection() {
        SqlConnectionHelper.closeConnection(connection);
    }

    @Override
    public AdministratorDAO getAdministratorDAO() {
        return new AdministratorPostgreSQLDAO(connection);
    }
}

