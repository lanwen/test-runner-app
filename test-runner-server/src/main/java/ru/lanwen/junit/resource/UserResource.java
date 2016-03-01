package ru.lanwen.junit.resource;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Component;
import ru.lanwen.junit.entity.User;
import ru.lanwen.junit.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("user")
@Component
@Produces(APPLICATION_JSON)
public class UserResource {

    @Inject
    private UserService usr;

    @Produce(uri = "seda:process")
    private ProducerTemplate template;

    @GET
    public User ok(@QueryParam("message") String message) {
        return new User();
    }
}
