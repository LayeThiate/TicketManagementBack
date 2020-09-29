package fr.urss.security.exception;

/**
 * Thrown if errors occur during the authorization process.
 *
 * @author lucas.david
 */
public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}
