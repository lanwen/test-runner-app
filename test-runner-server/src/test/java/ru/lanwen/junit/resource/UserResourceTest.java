package ru.lanwen.junit.resource;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UserResourceTest extends BasicResourceTest {

    @Test
    @Ignore
    public void shouldReturnUser() throws Exception {
        Response resp = target("user").queryParam("message", "some").request().get();

        assertThat(resp.getStatus(), is(HttpStatus.OK_200));
        assertThat(resp.readEntity(String.class), is("some"));
    }
}