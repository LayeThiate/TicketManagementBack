package fr.urss.security.exception;

/**
 * Thrown if an authentication token is invalid.
 */
public class InvalidAuthenticationTokenException extends AuthenticationException {
    public InvalidAuthenticationTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
