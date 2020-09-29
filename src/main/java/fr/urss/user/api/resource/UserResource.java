package fr.urss.user.api.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import fr.urss.common.api.mapper.MergePatchMapper;
import fr.urss.common.api.provider.ObjectMapperProvider;
import fr.urss.security.domain.Authority;
import fr.urss.security.exception.AuthenticationException;
import fr.urss.user.domain.User;
import fr.urss.user.service.UserService;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

/**
 * JAX-RS resource class that provides operations for users.
 *
 * @author lucas.david
 */
@Path("u")
@RequestScoped
public class UserResource {

    @Context
    private UriInfo uriInfo;

    @Context
    private SecurityContext securityContext;

    @Inject
    private UserService service;

    private ObjectMapper mapper = new ObjectMapperProvider().getContext(UserResource.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Administrator"})
    public Response getUsers() {
        return Response.ok(service.findAll()).build();
    }

    @GET
    @Path("{user: \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Administrator"})
    public Response getUser(@PathParam("user") long identifier) {
        return Response.ok(service.findById(identifier).orElseThrow(NotFoundException::new)).build();
    }

    @GET
    @Path("{user: (\\w|\\.)+}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Administrator"})
    public Response getUser(@PathParam("user") String username) {
        return Response.ok(service.findByUsername(username).orElseThrow(NotFoundException::new)).build();
    }

    @GET
    @Path("me")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getCurrentUser() {
        var principal = securityContext.getUserPrincipal();

        if (principal == null) /* should not happen. */
            throw new AuthenticationException("You need to authenticate first.");

        /* orElseThrow: token has probably been compromised. */
        System.err.println(principal.getName());
        return Response.ok(service.findByUsername(principal.getName()).orElseThrow(NotFoundException::new)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Administrator"})
    public Response createUser(User user) {
        return Response.created(uriInfo.getAbsolutePathBuilder().path(Long.toString(service.create(user))).build())
                       .build();
    }

    @PATCH
    @Path("{user: \\d+}")
    @Consumes("application/merge-patch+json")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Administrator"})
    public Response mergePatchUser(@PathParam("user") int identifier, JsonNode mergePatch) {
        var user = service.findById(identifier).orElseThrow(NotFoundException::new);

        try {
            service.update(
                    ((MergePatchMapper) mapper).mergePatch(JsonMergePatch.fromJson(mergePatch), user, User.class));
        } catch (JsonPatchException ignored) {
        }

        return Response.accepted().build();
    }

    @PATCH
    @Path("{user: (\\w|\\.)+}")
    @Consumes("application/merge-patch+json")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Administrator"})
    public Response mergePatchUser(@PathParam("user") String username, JsonNode mergePatch) {
        var user = service.findByUsername(username).orElseThrow(NotFoundException::new);

        try {
            service.update(
                    ((MergePatchMapper) mapper).mergePatch(JsonMergePatch.fromJson(mergePatch), user, User.class));
        } catch (JsonPatchException ignored) {
        }

        return Response.accepted().build();
    }

    @DELETE
    @Path("{user: \\d+}")
    @RolesAllowed({"Administrator"})
    public Response deleteUser(@PathParam("user") int identifier) {
        service.delete(service.findById(identifier).orElseThrow(NotFoundException::new));
        return Response.ok().build();
    }

    @DELETE
    @Path("{user: (\\w|\\.)+}")
    @RolesAllowed({"Administrator"})
    public Response deleteUser(@PathParam("user") String username) {
        service.delete(service.findByUsername(username).orElseThrow(NotFoundException::new));
        return Response.ok().build();
    }

    /**
     * Authority management.
     */

    @PUT
    @Path("{user: (\\w|\\.)+}/authority")
    @RolesAllowed({"Administrator"})
    public Response promoteUser(@QueryParam("user") String username, String authority) {
        service.promote(username, Authority.valueOf(authority));
        return Response.ok().build();
    }

    @DELETE
    @Path("{user: (\\w|\\.)+}/authority")
    @RolesAllowed({"Administrator"})
    public Response demoteUser(@QueryParam("user") String username, String authority) {
        service.demote(username, Authority.valueOf(authority));
        return Response.ok().build();
    }

    /** Technician management. */

    @GET
    @Path("{user: (\\w|\\.)+}/my-tickets")
    @RolesAllowed({"Technician"})
    public Response getTechnicianTickets(@QueryParam("user") String username) {
        return Response.ok(service.findTechnicianTickets(username)).build();
    }

    @PUT
    @Path("{user: (\\w|\\.)+}/skill")
    @RolesAllowed({"Administrator"})
    public Response addSKillUser(@QueryParam("user") String username, String skill) {
        service.addSkill(username, skill);
        return Response.ok().build();
    }

    @DELETE
    @Path("{user: (\\w|\\.)+}/skill")
    @RolesAllowed({"Administrator"})
    public Response removeSkillUser(@QueryParam("user") String username, String skill) {
        service.removeSkill(username, skill);
        return Response.ok().build();
    }


    /**
     * TechnicianManager management.
     */

    @GET
    @Path("{user: (\\w|\\.)+}/supervised")
    @RolesAllowed({"Administrator"})
    public Response supervisedUser(@QueryParam("user") String username) {
        var user = service.findTechnicianManagerByUsername(username).orElseThrow(NotFoundException::new);
        return Response.ok(user.getSupervisedTechnician()).build();
    }

    @PUT
    @Path("{user: (\\w|\\.)+}/supervised")
    @RolesAllowed({"Administrator"})
    public Response setSupervisedUser(@QueryParam("user") String username, String toSuperviseUsername) {
        var user = service.findTechnicianManagerByUsername(username).orElseThrow(NotFoundException::new);
        var toSupervise = service.findTechnicianByUsername(toSuperviseUsername).orElseThrow(NotFoundException::new);
        user.setSupervisedTechnician(toSupervise);
        return Response.ok().build();
    }

    @DELETE
    @Path("{user: (\\w|\\.)+}/supervised")
    @RolesAllowed({"Administrator"})
    public Response deleteSupervisedUser(@QueryParam("user") String username) {
        var user = service.findTechnicianManagerByUsername(username).orElseThrow(NotFoundException::new);
        user.setSupervisedTechnician(null);
        return Response.accepted().build();
    }
}
