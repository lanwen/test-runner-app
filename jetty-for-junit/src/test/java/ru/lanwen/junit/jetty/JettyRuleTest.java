package ru.lanwen.junit.jetty;

import org.junit.Test;

/**
 * @author lanwen (Merkushev Kirill)
 */
public class JettyRuleTest {

    @Test
    public void shouldStartJettyWithEmptyContextAndCp() throws Throwable {
        JettyRule jettyRule = new JettyRule(0);
        
        jettyRule.before();
        try {
            jettyRule.port();
        } finally {
            jettyRule.after();
        }
        
    }
}
