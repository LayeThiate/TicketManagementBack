package fr.urss.security.api.resource;

import fr.urss.security.api.KonamiCodeBasedSecurityContext;
import fr.urss.security.api.TokenBasedSecurityContext;
import fr.urss.security.api.model.AuthenticationToken;
import fr.urss.security.api.model.Credentials;
import fr.urss.security.service.AuthenticationTokenService;
import fr.urss.security.service.CredentialsValidator;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * JAX-RS resource class that provides operations for authentication.
 *
 * @author lucas.david
 */
@Path("authenticate")
@RequestScoped
public class AuthenticationResource {

    @Context
    private SecurityContext context;

    @Inject
    private CredentialsValidator validator;

    @Inject
    private AuthenticationTokenService service;

    /**
     * Validate user credentials and issue a token for the user.
     *
     * @param credentials
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticate(Credentials credentials) {
        var user = validator.validate(credentials);
        var authenticationToken = new AuthenticationToken(
                service.issueToken(user.getUsername(), user.getAuthorities()));
        return Response.ok(authenticationToken).build();
    }

    /**
     * Refresh the authentication token for the current user.
     *
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll /* just requires to be authenticated. */ public Response refresh() {
        if (context instanceof KonamiCodeBasedSecurityContext)
            return Response.accepted().build(); /* accepted because Konami Code authentication is always valid. */

        var details = ((TokenBasedSecurityContext) context).getAuthenticationTokenDetails();
        var authenticationToken = new AuthenticationToken(service.refreshToken(details));
        return Response.ok(authenticationToken).build();
    }
}