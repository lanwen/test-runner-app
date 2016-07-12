package ru.lanwen.junit.resource;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.spi.AggregationRepository;
import org.springframework.stereotype.Component;
import ru.lanwen.junit.entity.Testcase;
import ru.lanwen.junit.entity.TestcaseState;
import ru.lanwen.junit.entity.TestcaseStatus;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("test")
@Component
@Produces(APPLICATION_JSON)
public class TestStateResource {

    public static final String STATUS = "status";

    @Inject
    private AggregationRepository repo;

    @Produce(uri = "seda:in")
    private ProducerTemplate in;

    @POST
    @Path("started")
    @Consumes(APPLICATION_JSON)
    public void started(Testcase testcase) {
        in.sendBody(testcase);
    }

    @POST
    @Path("passed")
    @Consumes(APPLICATION_JSON)
    public void passed(Testcase testcase) {
        in.sendBodyAndHeader(testcase.withState(TestcaseState.STARTED), STATUS, TestcaseStatus.PASSED);
    }

    @POST
    @Path("failed")
    @Consumes(APPLICATION_JSON)
    public void failed(Testcase testcase) {
        in.sendBodyAndHeader(testcase.withState(TestcaseState.FINISHED), STATUS, TestcaseStatus.FAILED);
    }

    @POST
    @Path("skipped")
    @Consumes(APPLICATION_JSON)
    public void skipped(Testcase testcase) {
        in.sendBodyAndHeader(testcase.withState(TestcaseState.FINISHED), STATUS, TestcaseStatus.SKIPPED);
    }

    @GET
    @Path("current")
    @Consumes(APPLICATION_JSON)
    public Response current() {
        return Response.ok(repo.getKeys()).build();
    }
}
