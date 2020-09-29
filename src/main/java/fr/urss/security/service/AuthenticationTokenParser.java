package fr.urss.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import fr.urss.security.api.AuthenticationTokenDetails;
import fr.urss.security.domain.Authority;
import fr.urss.security.exception.InvalidAuthenticationTokenException;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.stream.Collectors;

/**
 * Component which provides operations for parsing JWT tokens.
 *
 * @author lucas.david
 */
@Dependent
class AuthenticationTokenParser {

    @Inject
    private AuthenticationTokenSettings settings;

    /**
     * Parse a JWT token.
     *
     * @param token
     * @return
     */
    public AuthenticationTokenDetails parseToken(final String token) {
        try {
            var algorithm = Algorithm.HMAC256(settings.getSecret());
            var claims = JWT.require(algorithm).withAudience(settings.getAudience())
                            .acceptLeeway(settings.getClockSkew()).build().verify(token);

            return new AuthenticationTokenDetails.Builder().withId(claims.getId()).withUsername(claims.getSubject())
                                                           .withAuthorities(
                                                                   claims.getClaim(settings.getAuthoritiesClaimName())
                                                                         .asList(String.class).stream()
                                                                         .map(Authority::valueOf)
                                                                         .collect(Collectors.toSet())).withIssuedDate(
                            ZonedDateTime.ofInstant(claims.getIssuedAt().toInstant(), ZoneOffset.UTC))
                                                           .withExpirationDate(ZonedDateTime.ofInstant(
                                                                   claims.getExpiresAt().toInstant(), ZoneOffset.UTC))
                                                           .withRefreshCount(
                                                                   claims.getClaim(settings.getRefreshCountClaimName())
                                                                         .asInt()).withRefreshLimit(
                            claims.getClaim(settings.getRefreshLimitClaimName()).asInt()).build();

        } catch (TokenExpiredException e) {
            throw new InvalidAuthenticationTokenException("Expired token.", e);
        } catch (InvalidClaimException e) {
            throw new InvalidAuthenticationTokenException("Invalid claim value.", e);
        } catch (Exception e) {
            throw new InvalidAuthenticationTokenException("Invalid token.", e);
        }
    }

}