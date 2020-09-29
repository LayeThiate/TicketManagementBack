package fr.urss;

import fr.urss.common.api.configuration.JerseyConfiguration;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentManager;
import org.glassfish.jersey.servlet.ServletContainer;
import org.jboss.weld.environment.servlet.Listener;

import javax.servlet.ServletException;
import java.util.logging.Logger;

import static io.undertow.servlet.Servlets.listener;

public class Application {


    public static void main(String[] args) {
        start(8080);

        /* fill(sessionFactory.openSession()); */
    }

    private static Undertow Server;
    private static DeploymentManager Manager;
    public static final int DefaultPort = 8080;
    public static final Logger AppLogger = Logger.getLogger(Application.class.getName());

    public static void start(int port) {
        AppLogger.info(String.format("Stating server on port %d.", port));

        var path = Handlers.path();

        Server = Undertow.builder().addHttpListener(port, "localhost").setHandler(path).build();

        Server.start();

        AppLogger.info(String.format("Server started on port %d.", port));

        var builder = Servlets.deployment().setClassLoader(Application.class.getClassLoader()).setContextPath("/")
                              .addListeners(listener(Listener.class))
                              .setResourceManager(new ClassPathResourceManager(Application.class.getClassLoader()))
                              .addServlets(Servlets.servlet("jerseyServlet", ServletContainer.class).setLoadOnStartup(1)
                                                   .addInitParam("javax.ws.rs.Application",
                                                                 JerseyConfiguration.class.getName())
                                                   .addMapping("/api/*")).setDeploymentName("application.war");

        AppLogger.info("Starting application deployment.");

        Manager = Servlets.defaultContainer().addDeployment(builder);
        Manager.deploy();

        try {
            path.addPrefixPath("/", Manager.start());
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }

        AppLogger.info("Application deployed.");

        //System.out.println(BCrypt.hashpw("secret-password", BCrypt.gensalt()));
    }

    public static void stop() {
        if (Server == null) throw new IllegalStateException("Server has not been started yet.");

        AppLogger.info("Stopping server.");

        Manager.undeploy();
        Server.stop();

        AppLogger.info("Server stopped.");
    }

}
