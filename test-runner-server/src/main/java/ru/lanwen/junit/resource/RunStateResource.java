package ru.lanwen.junit.resource;

import org.springframework.stereotype.Component;
import ru.lanwen.junit.service.CurrentStateService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("run")
@Component
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class RunStateResource {

    @Inject
    private CurrentStateService state;

    @GET
    @Path("state")
    public Response state() {
        return Response.ok(state.currentState()).build();
    }
}
