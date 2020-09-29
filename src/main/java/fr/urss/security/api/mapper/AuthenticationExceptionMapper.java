package fr.urss.security.api.mapper;

import fr.urss.common.api.model.APIError;
import fr.urss.security.exception.AuthenticationException;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Exception mapper for {@link AuthenticationException}s.
 *
 * @author lucas.david
 */
@Provider
public class AuthenticationExceptionMapper implements ExceptionMapper<AuthenticationException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(AuthenticationException exception) {
        var status = Response.Status.FORBIDDEN;

        var error = new APIError(status.getStatusCode(), status.getReasonPhrase(), exception.getMessage(),
                                 uriInfo.getAbsolutePath().getPath());

        return Response.status(status).entity(error).type(MediaType.APPLICATION_JSON).build();
    }
}