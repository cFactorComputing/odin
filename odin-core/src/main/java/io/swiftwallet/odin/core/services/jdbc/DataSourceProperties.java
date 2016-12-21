package io.swiftwallet.odin.core.services.jdbc;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author gibugeorge on 12/12/16.
 * @version 1.0
 */
@ConfigurationProperties(prefix = "odin.jdbc.data-source")
public class DataSourceProperties {

    private boolean enabled = false;
    private String names;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }
}
