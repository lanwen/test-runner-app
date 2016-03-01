package ru.lanwen.junit;

import org.junit.Test;
import ru.lanwen.junit.client.TestRunnerClientGenerated;
import ru.lanwen.junit.entity.User;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
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
        User user = TestRunnerClientGenerated.user().getAsUser();
        assertThat(user.getFirstName(), nullValue());
    }
}
