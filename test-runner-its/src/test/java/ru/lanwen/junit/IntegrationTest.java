package ru.lanwen.junit;

import org.junit.Test;
import ru.lanwen.junit.client.TestRunnerClientGenerated;
import ru.lanwen.junit.entity.Testcase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author lanwen (Merkushev Kirill)
 */
public class IntegrationTest {

    @Test
    public void shouldHandlePing() throws Exception {
        String pong = TestRunnerClientGenerated.ping().getAsTextPlain(String.class);
        assertThat(pong, is("pong"));
    }

    @Test
    public void shouldHandleUser() throws Exception {
        TestRunnerClientGenerated.test().state().postJson(new Testcase()).close();
    }
}
