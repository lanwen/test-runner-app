package ru.lanwen.junit.camel.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spi.AggregationRepository;
import org.springframework.stereotype.Component;
import ru.lanwen.junit.entity.Testcase;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;
import static org.apache.camel.LoggingLevel.DEBUG;
import static org.apache.camel.util.toolbox.AggregationStrategies.useLatest;
import static ru.lanwen.junit.resource.TestStateResource.STATUS;

/**
 * @author lanwen (Merkushev Kirill)
 */
@Component
public class TestStateRoute extends RouteBuilder {

    @Inject
    @Named("started")
    private AggregationRepository started;

    @Override
    public void configure() throws Exception {
        from("seda:in")
                .routeId("incoming")
                .process(exchange -> {
                    Testcase msg = exchange.getIn().getBody(Testcase.class);
                    exchange.getIn().setHeader("id", format("%s#%s", msg.getClassname(), msg.getMethodname()));
                })
                .log(DEBUG, "Incoming testcase ${header.id}!")
                .choice()
                .when(header(STATUS).isNull()).to("seda:started")
                .end()
                .aggregate(header("id"), useLatest())
                .completionPredicate(header(STATUS).isNotNull())
                .aggregationRepository(started)
                .completionTimeout(TimeUnit.MINUTES.toMillis(5))
                .to("seda:finished");
        
        from("seda:started").routeId("started")
                .log("Started new testcase ${header.id}!")
                .bean("broadcaster")
                .stop();

        from("seda:finished")
                .routeId("finished")
                .log("Finished with status [${header.status}] ${header.id}!")
                .bean("broadcaster")
                .stop();
    }
}
