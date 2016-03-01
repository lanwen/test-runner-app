package ru.lanwen.junit.resource;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PingResourceTest extends BasicResourceTest {

    @Test
    public void shouldReturnPong() throws Exception {
        Response resp = target("ping").request().get();

        assertThat(resp.getStatus(), is(HttpStatus.OK_200));
        assertThat(resp.readEntity(String.class), is("pong"));
    }
}