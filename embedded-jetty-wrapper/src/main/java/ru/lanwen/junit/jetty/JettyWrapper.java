package ru.lanwen.junit.jetty;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ShutdownHandler;
import org.eclipse.jetty.webapp.WebAppClassLoader;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author lanwen (Merkushev Kirill)
 */
public class JettyWrapper {

    private static Logger LOG = LoggerFactory.getLogger(JettyWrapper.class);

    public static final String SHUTDOWN_PASSWORD = "shutdown-this-jetty";

    private final ClassLoader parent;
    private final String[] additionalClassPath;

    private int port = 0;
    private Server server;


    /**
     * Create {@link JettyWrapper} with getClass().getClassLoader() parent.
     *
     * @param port given port for jetty server
     */
    public JettyWrapper(int port) {
        this(port, JettyWrapper.class.getClassLoader(), new String[]{});
    }

    /**
     * Create {@link JettyWrapper} with new URL class loader with given classpath
     *
     * @param classPath specified classpath for classloader
     */
    public JettyWrapper(int port, String... classPath) {
        this(port, JettyWrapper.class.getClassLoader(), classPath);
    }

    /**
     * Create {@link JettyWrapper} with specified parent classloader
     *
     * @param parent given parent classloader
     */
    public JettyWrapper(int port, ClassLoader parent, String[] additionalClassPath) {
        this.parent = parent;
        this.additionalClassPath = unify(additionalClassPath);
        this.port = port;
    }

    public int port() {
        return port;
    }

    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            throw new RuntimeException("Can't stop jetty", e);
        }
    }


    /**
     * Start jetty server
     *
     * @param webApp      path to web application directory
     * @param contextPath given context path for jetty server
     * @param waitFor     true if need to join to server, false otherwise
     *
     * @throws Exception if can't start server with specified configuration
     */
    public void run(String webApp, String contextPath, boolean waitFor) throws Exception {
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        server = new Server(port);

        WebAppContext context = new WebAppContext();
        context.setContextPath(contextPath);
        context.setWar(webApp);
        context.setParentLoaderPriority(true);
        context.setClassLoader(createWebAppClassLoader(context));

        server.setHandler(createHandlersForServer(context));
        server.start();

        port = ((ServerConnector) server.getConnectors()[0]).getLocalPort();

        if (waitFor) {
            LOG.info("Waiting until process is finished...");
            server.join();
        }
    }

    /**
     * Create {@link WebAppClassLoader} with {@link #parent} as parent class loader and with extra classpath
     *
     * @param context {@link WebAppContext} to set to class loader
     *
     * @return created class loader
     * @throws IOException if can't read resource from one of given extra class path elements
     */
    private WebAppClassLoader createWebAppClassLoader(WebAppContext context) throws IOException {
        WebAppClassLoader classLoader = new WebAppClassLoader(parent, context) {
            @Override
            protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
                try {
                    return super.loadClass(name, resolve);
                } catch (NoClassDefFoundError e) {
                    LOG.trace("Classloader exception", e);
                    Class<?> c = super.findClass(name);
                    if (resolve) {
                        resolveClass(c);
                    }
                    return c;
                }
            }
        };
        for (String url : additionalClassPath) {
            classLoader.addClassPath(url);
        }
        return classLoader;
    }

    /**
     * Unify given strings
     *
     * @param strings specified strings to unify
     *
     * @return array of string without duplicates
     */
    public static String[] unify(String... strings) {
        return Arrays.stream(strings).distinct().toArray(String[]::new);
    }

    /**
     * Create {@link org.eclipse.jetty.server.handler.HandlerList} for given jetty server and web application context
     *
     * @param context specified web application context
     *
     * @return created handlers
     */
    private static HandlerList createHandlersForServer(WebAppContext context) {
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{new ShutdownHandler(SHUTDOWN_PASSWORD), context});
        return handlers;
    }


}
