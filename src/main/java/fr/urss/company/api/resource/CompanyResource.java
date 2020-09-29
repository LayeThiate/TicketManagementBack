package fr.urss.company.api.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import fr.urss.common.api.mapper.MergePatchMapper;
import fr.urss.common.api.provider.ObjectMapperProvider;
import fr.urss.company.domain.Company;
import fr.urss.company.service.CompanyService;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Objects;

/**
 * JAX-RS resource class that provides operations for {@link Company}s.
 *
 * @author lucas.david
 */
@Path("c")
@RequestScoped
public class CompanyResource {

    @Inject
    CompanyService service;

    @Context
    private UriInfo uriInfo;

    private ObjectMapper mapper = new ObjectMapperProvider().getContext(CompanyResource.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Administrator", "TechnicianManager", "Operator"})
    public Response getAllCompanies() {
        return Response.ok(service.findAll()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Administrator"})
    public Response createCompany(Company company) {
        return Response.created(uriInfo.getAbsolutePathBuilder().path(Long.toString(service.create(company))).build())
                       .build();
    }

    @GET
    @Path("{company: \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Administrator", "TechnicianManager", "Operator"})
    public Response getCompany(@PathParam("company") long identifier) {
        return Response.ok(service.findById(identifier).orElseThrow(NotFoundException::new)).build();
    }

    @GET
    @Path("{company: (\\w|\\.)+}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Administrator", "TechnicianManager", "Operator"})
    public Response getCompany(@PathParam("company") String name) {
        return Response.ok(service.findByName(name).orElseThrow(NotFoundException::new)).build();
    }

    @GET
    @Path("{company: \\d+}/e")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Administrator", "TechnicianManager", "Operator"})
    public Response getCompanyEmployees(@PathParam("company") long identifier) {
        return Response.ok(service.findById(identifier).orElseThrow(NotFoundException::new).getEmployees()).build();
    }

    @GET
    @Path("{company: (\\w|\\.)+}/e")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Administrator", "TechnicianManager", "Operator"})
    public Response getCompanyEmployees(@PathParam("company") String name) {
        return Response.ok(service.findByName(name).orElseThrow(NotFoundException::new).getEmployees()).build();
    }


    @GET
    @Path("{company: \\d+}/e/{customer: \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Administrator", "TechnicianManager", "Operator"})
    public Response getCompanyEmployee(@PathParam("company") long companyIdentifier,
            @PathParam("customer") long customerIdentifier) {
        var company = service.findById(companyIdentifier).orElseThrow(NotFoundException::new);
        return Response
                .ok(company.getEmployees().stream().filter(c -> c.getIdentifier() == customerIdentifier).findFirst()
                           .orElseThrow(NotFoundException::new)).build();
    }

    @GET
    @Path("{company: (\\w|\\.)+}/e/{customer: (\\w|\\.)+}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Administrator", "TechnicianManager", "Operator"})
    public Response getCompanyEmployee(@PathParam("company") String name, @PathParam("customer") String username) {
        var company = service.findByName(name).orElseThrow(NotFoundException::new);
        return Response
                .ok(company.getEmployees().stream().filter(c -> Objects.equals(c.getUsername(), username)).findFirst()
                           .orElseThrow(NotFoundException::new)).build();
    }

    @PATCH
    @Path("{company: (\\w|\\.)+}")
    @Consumes("application/merge-patch+json")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Administrator"})
    public Response mergePatchCompany(@PathParam("company") String username, JsonNode mergePatch) {
        var company = service.findByName(username).orElseThrow(NotFoundException::new);

        try {
            service.update(((MergePatchMapper) mapper)
                                   .mergePatch(JsonMergePatch.fromJson(mergePatch), company, Company.class));
        } catch (JsonPatchException ignored) {
        }

        return Response.accepted().build();
    }

    @PATCH
    @Path("{company: \\d+}")
    @Consumes("application/merge-patch+json")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Administrator"})
    public Response mergePatchCompany(@PathParam("company") long id, JsonNode mergePatch) {
        var company = service.findById(id).orElseThrow(NotFoundException::new);

        try {
            service.update(((MergePatchMapper) mapper)
                                   .mergePatch(JsonMergePatch.fromJson(mergePatch), company, Company.class));
        } catch (JsonPatchException ignored) {
        }

        return Response.accepted().build();
    }

    @DELETE
    @Path("{company: \\d+}")
    @RolesAllowed({"Administrator"})
    public Response deleteCompany(@PathParam("company") long id) {
        service.delete(service.findById(id).orElseThrow(NotFoundException::new));
        return Response.ok().build();
    }

    @DELETE
    @Path("{company: (\\w|\\.)+}")
    @RolesAllowed({"Administrator"})
    public Response deleteCompany(@PathParam("company") String name) {
        service.delete(service.findByName(name).orElseThrow(NotFoundException::new));
        return Response.ok().build();
    }

}
