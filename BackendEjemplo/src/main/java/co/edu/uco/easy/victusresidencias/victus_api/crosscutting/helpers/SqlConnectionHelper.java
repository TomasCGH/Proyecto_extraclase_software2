package co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers;

import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.UcoApplicationException;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.exceptions.enums.Layer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class SqlConnectionHelper {
    // Common error messages
    private static final String UNEXPECTED_PROBLEM_USER_MESSAGE = "Se ha presentado un problema inesperado tratando de llevar a cabo la operación deseada...";
    private static final String UNEXPECTED_PROBLEM_ALTERNATIVE_USER_MESSAGE = "Se ha presentado un problema inesperado, tratando de llevar a cabo la operación deseada...";
    private static final String SQL_EXCEPTION_TECHNICAL_MESSAGE = "Se ha presentado una excepción de tipo SQLException tratando de %s. Por favor revise el log de errores para tener más detalles del error presentado...";
	private static final String SQL_EXCEPTION_TECHNICAL_MESSAGE_NEXT = " obtener la conexión con la fuente de datos SQL deseada...";

	private SqlConnectionHelper() {
        // Private constructor to prevent instantiation
	}

	public static boolean connectionIsNull(final Connection connection) {
		return ObjectHelper.isNull(connection);
	}

	public static boolean connectionIsOpen(final Connection connection) {
		try {
			return !connectionIsNull(connection) && !connection.isClosed();
		} catch (final SQLException exception) {
			var userMessage = UNEXPECTED_PROBLEM_USER_MESSAGE;
			var technicalMessage = String.format(SQL_EXCEPTION_TECHNICAL_MESSAGE, "llevar a cabo la validación de si la conexión estaba o no abierta");
			throw new UcoApplicationException(userMessage, technicalMessage, exception, Layer.DATA);
		}
	}

	public static void initTransaction(final Connection connection) {

		validateIfConnectionIsClosed(connection);

		try {

			if (!connection.getAutoCommit()) {
				var userMessage = UNEXPECTED_PROBLEM_ALTERNATIVE_USER_MESSAGE;
				var technicalMessage = "No es posible iniciar una transacción que ya ha sido iniciada previamente en la base de datos SQL deseada...";
				throw new UcoApplicationException(userMessage, technicalMessage, new Exception(), Layer.DATA);
			}

			connection.setAutoCommit(false);
		} catch (final SQLException exception) {
			var userMessage = UNEXPECTED_PROBLEM_USER_MESSAGE;
			var technicalMessage = String.format(SQL_EXCEPTION_TECHNICAL_MESSAGE, "iniciar la transacción");
			throw new UcoApplicationException(userMessage, technicalMessage, exception, Layer.DATA);
		}
	}

	public static void commitTransaction(final Connection connection) {

		validateIfConnectionIsClosed(connection);
		validateIfTransactionWasNotInitiated(connection);

		try {
			connection.commit();
		} catch (final SQLException exception) {
			var userMessage = UNEXPECTED_PROBLEM_USER_MESSAGE;
			var technicalMessage = String.format(SQL_EXCEPTION_TECHNICAL_MESSAGE, "confirmar la transacción");
			throw new UcoApplicationException(userMessage, technicalMessage, exception, Layer.DATA);
		}
	}

	public static void rollbackTransaction(final Connection connection) {

		validateIfConnectionIsClosed(connection);
		validateIfTransactionWasNotInitiated(connection);

		try {
			connection.rollback();
		} catch (final SQLException exception) {
			var userMessage = UNEXPECTED_PROBLEM_USER_MESSAGE;
			var technicalMessage = String.format(SQL_EXCEPTION_TECHNICAL_MESSAGE, "cancelar la transacción");
			throw new UcoApplicationException(userMessage, technicalMessage, exception, Layer.DATA);
		}
	}

	public static void validateIfConnectionIsOpen(final Connection connection) {
		if (SqlConnectionHelper.connectionIsOpen(connection)) {
			var userMessage = UNEXPECTED_PROBLEM_ALTERNATIVE_USER_MESSAGE;
			var technicalMessage = "No es posible tratar de abrir una conexión hacia la base de datos SQL que ya está abierta...";
			throw new UcoApplicationException(userMessage, technicalMessage, new Exception(), Layer.DATA);
		}
	}

	public static void validateIfConnectionIsClosed(final Connection connection) {
		if (!SqlConnectionHelper.connectionIsOpen(connection)) {
			var userMessage = UNEXPECTED_PROBLEM_ALTERNATIVE_USER_MESSAGE;
			var technicalMessage = "La conexión hacia la base de datos SQL está cerrada. Por tanto no es posible llevar a cabo la operación deseada...";
			throw new UcoApplicationException(userMessage, technicalMessage, new Exception(), Layer.DATA);
		}
	}

	public static void validateIfTransactionWasNotInitiated(final Connection connection) {
		try {
			if (connection.getAutoCommit()) {
				var userMessage = UNEXPECTED_PROBLEM_ALTERNATIVE_USER_MESSAGE;
				var technicalMessage = "La transacción no ha sido iniciada previamente para llevar a cabo la operación deseada en la base de datos SQL deseada...";
				throw new UcoApplicationException(userMessage, technicalMessage, new Exception(), Layer.DATA);
			}
		} catch (final SQLException exception) {
			var userMessage = UNEXPECTED_PROBLEM_USER_MESSAGE;
			var technicalMessage = String.format(SQL_EXCEPTION_TECHNICAL_MESSAGE, "validar si la transacción fue iniciada con la fuente de datos SQL deseada");
			throw new UcoApplicationException(userMessage, technicalMessage, exception, Layer.DATA);
		}
	}

	public static void closeConnection(final Connection connection) {

		validateIfConnectionIsClosed(connection);

		try {
			connection.close();
		} catch (final SQLException exception) {
			var userMessage = UNEXPECTED_PROBLEM_USER_MESSAGE;
			var technicalMessage = String.format(SQL_EXCEPTION_TECHNICAL_MESSAGE, "cerrar la conexión con la fuente de datos SQL deseada");
			throw new UcoApplicationException(userMessage, technicalMessage, exception, Layer.DATA);
		}
	}

	public static Connection openConnection(final String connectionString) {

		try {
			return DriverManager.getConnection(connectionString);
		} catch (final SQLException exception) {
			var userMessage = UNEXPECTED_PROBLEM_USER_MESSAGE;
			var technicalMessage = String.format(SQL_EXCEPTION_TECHNICAL_MESSAGE, SQL_EXCEPTION_TECHNICAL_MESSAGE_NEXT);
			throw new UcoApplicationException(userMessage, technicalMessage, exception, Layer.DATA);
		}
	}
	
	public static Connection openConnectionPostgreSQL(final String url,final String user,final String password) {

		try {
			return DriverManager.getConnection(url,user,password);
		} catch (final SQLException exception) {
			var userMessage = UNEXPECTED_PROBLEM_USER_MESSAGE;
			var technicalMessage = String.format(SQL_EXCEPTION_TECHNICAL_MESSAGE, SQL_EXCEPTION_TECHNICAL_MESSAGE_NEXT);
			throw new UcoApplicationException(userMessage, technicalMessage, exception, Layer.DATA);
		}
	}
	public static Connection openConnectionPostgreSQL(final String url) {

		try {
			return DriverManager.getConnection(url);
		} catch (final SQLException exception) {
			var userMessage = UNEXPECTED_PROBLEM_USER_MESSAGE;
			var technicalMessage = String.format(SQL_EXCEPTION_TECHNICAL_MESSAGE, SQL_EXCEPTION_TECHNICAL_MESSAGE_NEXT);
			throw new UcoApplicationException(userMessage, technicalMessage, exception, Layer.DATA);
		}
	}
	
	

}
