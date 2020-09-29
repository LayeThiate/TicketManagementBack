package fr.urss.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import fr.urss.security.api.AuthenticationTokenDetails;
import fr.urss.security.domain.Authority;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.Date;


/**
 * Component which provides operations for issuing JWT tokens.
 *
 * @author lucas.david
 */
@Dependent
public class AuthenticationTokenIssuer {

    @Inject
    private AuthenticationTokenSettings settings;

    /**
     * Issue a JWT token.
     *
     * @param details
     * @return
     */
    public String issueToken(AuthenticationTokenDetails details) {
        var algorithm = Algorithm.HMAC256(settings.getSecret());
        return JWT.create().withJWTId(details.getId()).withIssuer(settings.getIssuer())
                  .withAudience(settings.getAudience()).withSubject(details.getUsername())
                  .withIssuedAt(Date.from(details.getIssuedDate().toInstant()))
                  .withExpiresAt(Date.from(details.getExpirationDate().toInstant()))
                  .withArrayClaim(settings.getAuthoritiesClaimName(),
                                  details.getAuthorities().stream().map(Authority::toString).toArray(String[]::new))
                  .withClaim(settings.getRefreshCountClaimName(), details.getRefreshCount())
                  .withClaim(settings.getRefreshLimitClaimName(), details.getRefreshLimit()).sign(algorithm);
    }
}
