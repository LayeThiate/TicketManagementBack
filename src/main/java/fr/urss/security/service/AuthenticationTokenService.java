package fr.urss.security.service;

import fr.urss.common.configuration.Configurable;
import fr.urss.security.api.AuthenticationTokenDetails;
import fr.urss.security.domain.Authority;
import fr.urss.security.exception.AuthenticationTokenRefreshmentException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * Service which provides operations for authentication tokens.
 *
 * @author lucas.david
 */
@ApplicationScoped
public class AuthenticationTokenService {

    /**
     * How long the token is valid for (in seconds).
     */
    @Inject
    @Configurable("authentication.jwt.validFor")
    private Long validFor;

    /**
     * How many times the token can be refreshed.
     */
    @Inject
    @Configurable("authentication.jwt.refreshLimit")
    private Integer refreshLimit;

    @Inject
    private AuthenticationTokenIssuer issuer;

    @Inject
    private AuthenticationTokenParser parser;

    /**
     * Issue a token for a user with the given authorities.
     *
     * @param username
     * @param authorities
     * @return
     */
    public String issueToken(String username, Set<Authority> authorities) {
        var issuedDate = ZonedDateTime.now();
        var expirationDate = calculateExpirationDate(issuedDate);

        var details = new AuthenticationTokenDetails.Builder().withId(generateTokenIdentifier()).withUsername(username)
                                                              .withAuthorities(authorities).withIssuedDate(issuedDate)
                                                              .withExpirationDate(expirationDate).withRefreshCount(0)
                                                              .withRefreshLimit(refreshLimit).build();

        return issuer.issueToken(details);
    }

    /**
     * Parse and validate the token.
     *
     * @param token
     * @return
     */
    public AuthenticationTokenDetails parseToken(String token) {
        return parser.parseToken(token);
    }

    /**
     * Refresh a token.
     *
     * @param details
     * @return
     */
    public String refreshToken(AuthenticationTokenDetails details) {

        if (!details.isEligibleForRefreshment()) {
            throw new AuthenticationTokenRefreshmentException("This token cannot be refreshed.");
        }

        var issuedDate = ZonedDateTime.now();
        var expirationDate = calculateExpirationDate(issuedDate);

        var newDetails = new AuthenticationTokenDetails.Builder().withId(details.getId()) // Reuse the same id
                                                                 .withUsername(details.getUsername())
                                                                 .withAuthorities(details.getAuthorities())
                                                                 .withIssuedDate(issuedDate)
                                                                 .withExpirationDate(expirationDate)
                                                                 .withRefreshCount(details.getRefreshCount() + 1)
                                                                 .withRefreshLimit(refreshLimit).build();

        return issuer.issueToken(newDetails);
    }

    /**
     * Calculate the expiration date for a token.
     *
     * @param issuedDate
     * @return
     */
    private ZonedDateTime calculateExpirationDate(ZonedDateTime issuedDate) {
        return issuedDate.plusSeconds(validFor);
    }

    /**
     * Generate a token identifier.
     *
     * @return
     */
    private String generateTokenIdentifier() {
        return UUID.randomUUID().toString();
    }
}