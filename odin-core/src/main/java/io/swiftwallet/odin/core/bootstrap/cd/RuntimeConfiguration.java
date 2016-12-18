package io.swiftwallet.odin.core.bootstrap.cd;

import java.util.Map;

/**
 * @author gibugeorge on 11/12/16.
 * @version 1.0
 */
public class RuntimeConfiguration {

    private final Map<String, Object> configurations;


    public RuntimeConfiguration(final Map<String, Object> configurations) {
        this.configurations = configurations;
    }

    public Map<String, Object> getConfigurations() {
        return configurations;
    }
}
