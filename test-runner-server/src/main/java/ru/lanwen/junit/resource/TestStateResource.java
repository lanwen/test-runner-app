package ru.lanwen.junit.resource;

import org.apache.camel.Produce;
import org.springframework.stereotype.Component;
import ru.lanwen.junit.entity.Testcase;
import ru.lanwen.junit.service.TestcaseQueue;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("test")
@Component
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class TestStateResource {

    @Produce(uri = "seda:in")
    private TestcaseQueue queue;

    @POST
    @Path("state")
    public void state(Testcase testcase) {
        queue.enqueue(testcase);
    }
}
