package fr.urss.security.exception;

/**
 * Thrown if an authentication token cannot be refreshed.
 */
public class AuthenticationTokenRefreshmentException extends AuthenticationException {

    public AuthenticationTokenRefreshmentException(String message) {
        super(message);
    }

    public AuthenticationTokenRefreshmentException(String message, Throwable cause) {
        super(message, cause);
    }
}