package ru.lanwen.junit.resource;

import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import ru.lanwen.junit.MainApplication;

import javax.inject.Inject;
import javax.ws.rs.core.Application;

import static org.springframework.util.ReflectionUtils.doWithFields;
import static org.springframework.util.ReflectionUtils.makeAccessible;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/spring/application-context.xml", "classpath:test-service-context.xml"})
public abstract class BasicResourceTest extends JerseyTest {

    public BasicResourceTest() {
        super(new Application());
    }

    @Inject
    private void setContext(ApplicationContext context) {
        doWithFields(
                super.getClass(),
                field -> {
                    makeAccessible(field);
                    field.set(this, DeploymentContext.builder(configure(context)).build());
                },
                field -> field.getType().equals(DeploymentContext.class)
        );
    }

    protected ResourceConfig configure(ApplicationContext context) {
        forceSet(TestProperties.CONTAINER_PORT, "0");
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);

        return new MainApplication()
                .property("contextConfig", context)
                .packages(MainApplication.class.getPackage().getName());
    }

    @Override
    protected final DeploymentContext configureDeployment() {
        throw new UnsupportedOperationException("This method should be never called");
    }

    @Override
    protected final Application configure() {
        throw new UnsupportedOperationException("Please use configure(ApplicationContext) method to setup application");
    }

    protected void configureClient(ClientConfig config) {
        config.register(JacksonFeature.class);
    }
}
