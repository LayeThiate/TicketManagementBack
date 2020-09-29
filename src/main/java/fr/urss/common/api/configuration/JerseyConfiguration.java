package fr.urss.common.api.configuration;

import fr.urss.common.api.mapper.NotFoundExceptionMapper;
import fr.urss.common.api.provider.ObjectMapperProvider;
import fr.urss.company.api.resource.CompanyResource;
import fr.urss.security.api.filter.AuthenticationFilter;
import fr.urss.security.api.filter.AuthorizationFilter;
import fr.urss.security.api.mapper.AccessDeniedExceptionMapper;
import fr.urss.security.api.mapper.AuthenticationExceptionMapper;
import fr.urss.security.api.resource.AuthenticationResource;
import fr.urss.ticket.api.resource.TicketResource;
import fr.urss.user.api.resource.UserResource;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

/**
 * Jersey configuration class.
 *
 * @author lucas.david
 */
@ApplicationPath("api")
public class JerseyConfiguration extends ResourceConfig {

    public JerseyConfiguration() {
        register(AuthenticationResource.class);
        register(UserResource.class);
        register(CompanyResource.class);
        register(TicketResource.class);

        register(AuthenticationFilter.class);
        register(AuthorizationFilter.class);

        register(AccessDeniedExceptionMapper.class);
        register(AuthenticationExceptionMapper.class);
        register(NotFoundExceptionMapper.class);

        register(ObjectMapperProvider.class);
        register(JacksonFeature.class);
    }

}
