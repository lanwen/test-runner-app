package ru.lanwen.junit.jetty;

import org.junit.rules.ExternalResource;

/**
 * @author lanwen (Merkushev Kirill)
 */
public class JettyRule extends ExternalResource {
    private final JettyWrapper wrapper;

    private String webApp;
    private String contextPath;

    public JettyRule(int port, String... classpass) {
        wrapper = new JettyWrapper(port, classpass);
    }

    public JettyRule withWebApp(String webApp) {
        this.webApp = webApp;
        return this;
    }

    public JettyRule withContextPath(String contextPath) {
        this.contextPath = contextPath;
        return this;
    }

    public int port() {
        return wrapper.port();
    }

    @Override
    protected void before() throws Throwable {
        wrapper.run(webApp, contextPath, false);
    }

    @Override
    protected void after() {
        wrapper.stop();
    }
}
