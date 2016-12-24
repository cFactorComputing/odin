package io.swiftwallet.odin.core.bootstrap;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by gibugeorge on 24/12/2016.
 */
@ConfigurationProperties(prefix = "micro-service")
public class MicroServiceProperties {

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
