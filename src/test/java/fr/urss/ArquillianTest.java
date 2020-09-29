package fr.urss;

import fr.urss.common.api.config.JerseyConfig;
import fr.urss.security.api.model.AuthenticationToken;
import fr.urss.security.api.model.Credentials;

import io.undertow.servlet.Servlets;
import org.glassfish.jersey.servlet.ServletContainer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.undertow.api.UndertowWebArchive;
import org.jboss.weld.environment.servlet.Listener;
import org.junit.Before;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import java.net.URI;

import static io.undertow.servlet.Servlets.deployment;
import static io.undertow.servlet.Servlets.listener;

/**
 * Base Arquillian test class.
 *
 * @author lucas.david
 */
public abstract class ArquillianTest {
    @ArquillianResource
    protected URI uri;

    protected Client client;

    @Deployment(testable = false)
    public static Archive<WebArchive> createDeployment() {
        return ShrinkWrap.create(UndertowWebArchive.class)
                         .from(deployment().setClassLoader(Application.class.getClassLoader()).setContextPath("/")
                                           .addListeners(listener(Listener.class)).addServlets(
                                         Servlets.servlet("jerseyServlet", ServletContainer.class).setLoadOnStartup(1)
                                                 .addInitParam("javax.ws.rs.Application", JerseyConfig.class.getName())
                                                 .addMapping("/api/*")).setDeploymentName("application.war"));
    }

    @Before
    public void beforeTest() {
        this.client = ClientBuilder.newClient();
    }

    protected String getTokenForAdministrator() {
        var credentials = new Credentials();
        credentials.setUsername("lucas.david");
        credentials.setPassword("secret-password");

        var authenticationToken = client.target(uri).path("api").path("authenticate").request()
                                        .post(Entity.entity(credentials, MediaType.APPLICATION_JSON),
                                              AuthenticationToken.class);
        return authenticationToken.getToken();
    }


    protected String getTokenForOperator() {
        var credentials = new Credentials();
        credentials.setUsername("dusbat1502");
        credentials.setPassword("secret-password");

        var authenticationToken = client.target(uri).path("api").path("authenticate").request()
                                                        .post(Entity.entity(credentials, MediaType.APPLICATION_JSON),
                                                              AuthenticationToken.class);
        return authenticationToken.getToken();
    }

    protected String composeAuthorizationHeader(final String authenticationToken) {
        return "Bearer " + authenticationToken;
    }
}
