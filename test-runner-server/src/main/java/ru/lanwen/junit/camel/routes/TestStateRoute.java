package ru.lanwen.junit.camel.routes;

import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spi.AggregationRepository;
import org.apache.camel.util.toolbox.AggregationStrategies;
import org.springframework.stereotype.Component;
import ru.lanwen.junit.camel.beans.TestcaseFinishedPredicate;
import ru.lanwen.junit.camel.beans.UntilFinishedAggregation;
import ru.lanwen.junit.service.FinishedRepository;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

import static org.apache.camel.LoggingLevel.DEBUG;
import static org.apache.camel.builder.PredicateBuilder.not;

/**
 * @author lanwen (Merkushev Kirill)
 */
@Component
public class TestStateRoute extends RouteBuilder {

    @EndpointInject(uri = "seda:in")
    private Endpoint in;

    @EndpointInject(uri = "seda:started")
    private Endpoint started;

    @EndpointInject(uri = "seda:finished")
    private Endpoint finished;

    @EndpointInject(uri = "direct:aggregation")
    private Endpoint aggregation;


    @Inject
    private AggregationRepository startedR;

    @Inject
    private FinishedRepository finishedR;

    @Override
    public void configure() throws Exception {
        from(in)
                .routeId("incoming")
                .setHeader("id", simple("${body.classname}#${body.methodname}"))
                .log(DEBUG, "Incoming testcase ${header.id}!")
                .choice()
                .when(not(method(TestcaseFinishedPredicate.class)))
                .multicast().to(aggregation, started).parallelProcessing().end()
                .endChoice().otherwise().to(aggregation).end();

        from(aggregation)
                .routeId("aggregation")
                .aggregate(header("id"), AggregationStrategies.bean(UntilFinishedAggregation.class))
                .completionPredicate(method(TestcaseFinishedPredicate.class))
                .aggregationRepository(startedR)
                .completionTimeout(TimeUnit.MINUTES.toMillis(5))
                .to(finished);

        from(started)
                .routeId("started")
                .log("Started new testcase ${header.id}!")
                .bean("broadcaster")
                .stop();

        from(finished)
                .routeId("finished")
                .log("Finished with status [${body.status}] ${header.id}!")
                .bean(finishedR)
                .bean("broadcaster")
                .stop();
    }
}
