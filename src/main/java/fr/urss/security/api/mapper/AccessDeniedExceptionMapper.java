package fr.urss.security.api.mapper;

import fr.urss.common.api.model.APIError;
import fr.urss.security.exception.AccessDeniedException;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Exception mapper for {@link AccessDeniedException}s.
 *
 * @author lucas.david
 */
@Provider
public class AccessDeniedExceptionMapper implements ExceptionMapper<AccessDeniedException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(AccessDeniedException exception) {
        var status = Response.Status.FORBIDDEN;

        var error = new APIError(status.getStatusCode(), status.getReasonPhrase(), exception.getMessage(),
                                 uriInfo.getAbsolutePath().getPath());

        return Response.status(status).entity(error).type(MediaType.APPLICATION_JSON).build();
    }
}