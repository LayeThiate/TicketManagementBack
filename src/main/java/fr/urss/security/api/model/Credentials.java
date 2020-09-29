package fr.urss.security.api.model;

/**
 * API model for the credentials.
 *
 * @author lucas.david
 */
public class Credentials {

    private String username;
    private String password;

    public Credentials() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}