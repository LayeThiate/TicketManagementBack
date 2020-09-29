package fr.urss.security.service;

import fr.urss.security.api.model.Credentials;
import fr.urss.security.exception.AuthenticationException;
import fr.urss.user.domain.User;
import fr.urss.user.service.UserService;
import org.mindrot.jbcrypt.BCrypt;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class CredentialsValidator {

    @Inject
    private UserService userService;

    /**
     * Validate credentials.
     *
     * @param credentials
     * @return
     */
    public User validate(final Credentials credentials) {
        /* orElseThrow: User cannot be found with the given username/email. */
        var user = userService.findByUsername(credentials.getUsername())
                              .orElseThrow(() -> new AuthenticationException("Bad credentials."));

        if (!user.isActive()) /* User is not active. */ throw new AuthenticationException("The user is inactive.");

        if (!checkPassword(credentials.getPassword(), user.getPassword())) /* Invalid password. */
            throw new AuthenticationException("Bad credentials.");

        return user;
    }

    /**
     * Hashes a password using BCrypt.
     *
     * @param plainTextPassword
     * @return
     */
    private String hashPassword(String plainTextPassword) {
        final String salt = BCrypt.gensalt();
        return BCrypt.hashpw(plainTextPassword, salt);
    }

    /**
     * Checks a password against a stored hash using BCrypt.
     *
     * @param plainTextPassword
     * @param hashedPassword
     * @return
     */
    private boolean checkPassword(final String plainTextPassword, final String hashedPassword) {
        if (null == hashedPassword || !hashedPassword.startsWith("$2a$"))
            throw new RuntimeException("Hashed password is invalid");

        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}
