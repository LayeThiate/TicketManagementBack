package fr.urss.security.exception;

/**
 * Thrown if errors occur during the authentication process.
 *
 * @author lucas.david
 */
public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}