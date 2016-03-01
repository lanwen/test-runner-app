package ru.lanwen.junit.camel.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * @author lanwen (Merkushev Kirill)
 */
@Component
public class TestRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("seda:process")
                .log("incoming ${body}!")
                .to("stream:out");
    }
}
