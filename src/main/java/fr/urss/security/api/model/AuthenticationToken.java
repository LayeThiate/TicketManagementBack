package fr.urss.security.api.model;

/**
 * API model for the authentication token.
 *
 * @author lucas.david
 */
public class AuthenticationToken {

    private String token;

    public AuthenticationToken(final String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}