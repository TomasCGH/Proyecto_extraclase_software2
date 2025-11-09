package co.edu.uco.backendvictus.crosscutting.exception;

/**
 * Represents high level application errors raised in use cases.
 */
public class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ApplicationException(final String message) {
        super(message);
    }

    public ApplicationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
