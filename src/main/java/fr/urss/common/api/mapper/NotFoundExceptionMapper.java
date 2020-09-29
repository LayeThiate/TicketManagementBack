package fr.urss.common.api.mapper;

import fr.urss.common.api.model.APIError;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Exception mapper for {@link NotFoundException}s.
 *
 * @author lucas.david
 */
@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(NotFoundException exception) {
        var status = Response.Status.OK;

        var error = new APIError(status.getStatusCode(), status.getReasonPhrase(), exception.getMessage(),
                                 uriInfo.getAbsolutePath().getPath());

        return Response.status(status).entity(error).type(MediaType.APPLICATION_JSON).build();
    }
}