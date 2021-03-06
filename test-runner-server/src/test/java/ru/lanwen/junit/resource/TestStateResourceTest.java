package ru.lanwen.junit.resource;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;
import ru.lanwen.junit.entity.Testcase;
import ru.lanwen.junit.entity.TestcaseStatus;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestStateResourceTest extends BasicResourceTest {

    @Test
    public void shouldReturnUser() throws Exception {
        Response resp = target("test/state").request()
                .post(Entity.entity(new Testcase().withClassname(getClass().getName()), APPLICATION_JSON));

        target("test/state").request()
                .post(Entity.entity(new Testcase().withClassname(getClass().getName() + 1), APPLICATION_JSON));

        target("test/state").request()
                .post(Entity.entity(new Testcase().withClassname(getClass().getName() + 2), APPLICATION_JSON));

        target("test/state").request()
                .post(Entity.entity(new Testcase().withClassname(getClass().getName() + 2)
                        .withStatus(TestcaseStatus.FAILED), APPLICATION_JSON));

        String result = target("run/state").request().get(String.class);

        System.out.println(result);

        assertThat(resp.getStatus(), is(HttpStatus.NO_CONTENT_204));
    }
}