package fr.urss.ticket.api.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import fr.urss.common.api.mapper.MergePatchMapper;
import fr.urss.common.api.provider.ObjectMapperProvider;
import fr.urss.ticket.domain.Ticket;
import fr.urss.ticket.service.TicketService;
import fr.urss.user.api.resource.UserResource;
import fr.urss.user.domain.User;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;

@Path("t")
public class TicketResource {

    @Context
    private UriInfo uriInfo;

    @Inject
    private TicketService service;

    private ObjectMapper mapper = new ObjectMapperProvider().getContext(TicketResource.class);
	
	@Path("/tickets")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTickets(@Context UriInfo ui) {
		System.out.println("API FIND ALL TICKETS");
		//System.out.println(ui.getQueryParameters().keySet());
		return Response.ok(service.findAll(ui.getQueryParameters())).build();
	}

    @GET
    @Path("{ticket: \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Operator", "Technician", "TechnicianManager"})
    public Response getTicket(@PathParam("ticket") long identifier) {
        try {
            return Response.ok(service.findById(identifier).orElseThrow(NotFoundException::new)).build();
        } catch (NoResultException e) {
            return Response.ok(Response.status(Status.NOT_FOUND)).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Operator", "Technician", "TechnicianManager"})
    public Response getTickets() {
        return Response.ok(service.findAll()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed({"Operator", "Technician", "TechnicianManager"})
    public Response createTicket(Ticket ticket) {
        return Response.created(uriInfo.getAbsolutePathBuilder().path(Long.toString(service.create(ticket))).build())
                       .build();
    }


    @PATCH
    @Path("{ticket: \\d+}")
    @Consumes("application/merge-patch+json")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Administrator"})
    public Response mergePatchUser(@PathParam("ticket") long identifier, JsonNode mergePatch) {
        var ticket = service.findById(identifier).orElseThrow(NotFoundException::new);

        try {
            service.update(
                    ((MergePatchMapper) mapper).mergePatch(JsonMergePatch.fromJson(mergePatch), ticket, Ticket.class));
        } catch (JsonPatchException ignored) {}

        return Response.accepted().build();
    }

    @PATCH
    @Path("{ticket: \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Operator", "Technician", "TechnicianManager"})
    public Response updateTicket(@PathParam("ticket") long identifier) {
        return Response.ok().build();
    }

    @DELETE
    @Path("{ticket: \\d+}")
    @RolesAllowed({"Operator", "Technician", "TechnicianManager"})
    public Response deleteTicket(@PathParam("ticket") long identifier) {
        return Response.ok().build();
    }

    @PUT
    @Path("/{ticket}/close")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Technician", "TechnicianManager"})
    public Response closeTicket(long identifier) {
        return Response.ok().build();
    }
    
    @GET
    @Path("/categories")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategories() {
    	return Response.ok(service.findAllCategories()).build();
    }
}
