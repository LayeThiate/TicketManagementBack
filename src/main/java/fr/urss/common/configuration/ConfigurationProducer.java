package fr.urss.common.configuration;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.io.IOException;
import java.util.Properties;

/**
 * Read the <code>application.properties</code> file from the classpath and produce values that can be injected with
 *
 * @author lucas.david
 */
@ApplicationScoped
public class ConfigurationProducer {
    private Properties properties;

    @PostConstruct
    public void init() {
        properties = new Properties();

        var stream = ConfigurationProducer.class.getResourceAsStream("/application.properties");

        if (stream == null) throw new RuntimeException("Cannot find application.properties configuration file.");

        try {
            properties.load(stream);
        } catch (final IOException e) {
            throw new RuntimeException("Configuration file cannot be loaded.");
        }
    }

    @Produces
    @Configurable
    public String produceString(InjectionPoint ip) {
        return properties.getProperty(getKey(ip));
    }

    @Produces
    @Configurable
    public Integer produceInteger(InjectionPoint ip) {
        return Integer.valueOf(properties.getProperty(getKey(ip)));
    }

    @Produces
    @Configurable
    public Long produceLong(InjectionPoint ip) {
        return Long.valueOf(properties.getProperty(getKey(ip)));
    }

    @Produces
    @Configurable
    public Boolean produceBoolean(InjectionPoint ip) {
        return Boolean.valueOf(this.properties.getProperty(getKey(ip)));
    }

    private String getKey(InjectionPoint ip) {
        return ip.getAnnotated().getAnnotation(Configurable.class).value();
    }
}