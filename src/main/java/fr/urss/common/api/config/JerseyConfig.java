package fr.urss.common.api.config;

import fr.urss.common.api.provider.ObjectMapperProvider;
import fr.urss.ticket.api.resource.TicketResource;
import fr.urss.user.api.resource.UserResource;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;
import java.util.ArrayList;
import java.util.List;

/**
 * Jersey configuration class.
 *
 * @author lucas.david
 */
@ApplicationPath("api")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        List<Class<?>> list = new ArrayList<>();
        list.add(UserResource.class);
        list.add(TicketResource.class);
        register(list);

        register(ObjectMapperProvider.class);
        register(JacksonFeature.class);
    }

}
