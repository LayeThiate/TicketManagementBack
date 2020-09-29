package fr.urss.common.api.resource;

import fr.urss.common.domain.Skill;
import fr.urss.common.service.SkillService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * JAX-RS resource class that provides operations for {@link Skill}s.
 *
 * @author lucas.david
 */
@Path("skill")
@RequestScoped
public class SkillResource {

    @Inject
    private SkillService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSkills() {
        return Response.ok(service.findAll()).build();
    }

}
