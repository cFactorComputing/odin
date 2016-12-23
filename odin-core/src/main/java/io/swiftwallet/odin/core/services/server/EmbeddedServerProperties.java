package io.swiftwallet.odin.core.services.server;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by gibugeorge on 20/12/2016.
 */
@ConfigurationProperties(prefix = "server")
public class EmbeddedServerProperties {

    private int port=8080;
    private int jmxPort=1099;

    public int getPort() {
        return port;
    }

    public void setPort(final int port) {
        this.port = port;
    }

    public int getJmxPort() {
        return jmxPort;
    }

    public void setJmxPort(final int jmxPort) {
        this.jmxPort = jmxPort;
    }
}
