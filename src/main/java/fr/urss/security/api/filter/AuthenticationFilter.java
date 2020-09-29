package fr.urss.security.api.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.urss.common.api.provider.ObjectMapperProvider;
import fr.urss.security.api.AuthenticatedUserDetails;
import fr.urss.security.api.KonamiCodeBasedSecurityContext;
import fr.urss.security.api.TokenBasedSecurityContext;
import fr.urss.security.domain.Authority;
import fr.urss.security.exception.AuthenticationException;
import fr.urss.security.service.AuthenticationTokenService;
import fr.urss.user.service.UserService;

import javax.annotation.Priority;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * JWT authentication filter.
 *
 * @author lucas.david
 */
@Dependent
@Priority(Priorities.AUTHENTICATION)
@Provider
public class AuthenticationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Inject
    private UserService userService;

    @Inject
    private AuthenticationTokenService authenticationTokenService;

    private boolean isTokenBasedAuthentication(final String authorization) {
        /* Check if the Authorization header is valid.
           It must not be null and must be prefixed with "Bearer" plus a whitespace.
           The authentication scheme comparison must be case-insensitive. */
        return authorization != null && authorization.toLowerCase().startsWith("Bearer ".toLowerCase());
    }

    private void handleTokenBasedAuthentication(final String authenticationToken,
            final ContainerRequestContext requestContext) {
        var authenticationTokenDetails = authenticationTokenService.parseToken(authenticationToken);

        /* orElseThrow: User cannot be found with the given username. implies that token is corrupted or user became inactive. */
        var user = userService.findByUsername(authenticationTokenDetails.getUsername())
                              .orElseThrow(() -> new AuthenticationException("Invalid username."));

        var authenticatedUserDetails = new AuthenticatedUserDetails(user.getUsername(), user.getAuthorities());

        var isSecure = requestContext.getSecurityContext().isSecure();
        var securityContext = new TokenBasedSecurityContext(authenticatedUserDetails, authenticationTokenDetails,
                                                            isSecure);
        requestContext.setSecurityContext(securityContext);
    }

    /**
     * Cheat code for faster development.
     */
    private boolean isKonamiCodeBasedAuthentication(String authorization) {
        return authorization != null && authorization.startsWith("Konami Code ");
    }

    /**
     * Cheat code for faster development.
     */
    private void handleKonamiCodeBasedAuthentication(String authorization, ContainerRequestContext requestContext) {
        var mapper = new ObjectMapperProvider().getContext(Object.class);

        Set<Authority> authorities = Set.of();
        try {
            authorities = Arrays.stream(mapper.readValue(authorization, String[].class)).map(Authority::valueOf)
                                .collect(Collectors.toSet());
        } catch (JsonProcessingException ignored) {
        }

        var authenticatedUserDetails = new AuthenticatedUserDetails("↑↑↓↓←→←→□△", authorities);

        var isSecure = requestContext.getSecurityContext().isSecure();
        requestContext.setSecurityContext(new KonamiCodeBasedSecurityContext(authenticatedUserDetails, isSecure));
    }


    @Override
    public void filter(final ContainerRequestContext context) {
        var authorizationHeader = context.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (isTokenBasedAuthentication(authorizationHeader)) {
            handleTokenBasedAuthentication(authorizationHeader.substring("Bearer ".length()), context);
            return;
        }

        if (isKonamiCodeBasedAuthentication(authorizationHeader)) {
            handleKonamiCodeBasedAuthentication(authorizationHeader.substring("Konami Code ".length()), context);
            return;
        }

        /* throw new AuthenticationException("No authentication tool detected."); */
    }
}
