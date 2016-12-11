package io.swiftwallet.platform.core.bootstrap.cd;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author gibugeorge on 11/12/16.
 * @version 1.0
 */
@ConfigurationProperties(prefix = "swp.configuration-manager")
public class ConfigurationDiscoveryProperties {

    private String root = "configuration";
    private String runtimeConfiguration = "runtime";

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getRuntimeConfiguration() {
        return runtimeConfiguration;
    }

    public void setRuntimeConfiguration(String runtimeConfiguration) {
        this.runtimeConfiguration = runtimeConfiguration;
    }
}
