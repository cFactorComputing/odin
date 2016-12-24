package io.swiftwallet.odin.core.bootstrap.discovery;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author gibugeorge on 12/12/16.
 * @version 1.0
 */
@ConfigurationProperties(prefix = "service-discovery")
public class ServiceDiscoveryProperties {

    private boolean enabled=true;

    private String root="/services";

    private boolean register = true;

    private String id;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public boolean isRegister() {
        return register;
    }

    public void setRegister(boolean register) {
        this.register = register;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
