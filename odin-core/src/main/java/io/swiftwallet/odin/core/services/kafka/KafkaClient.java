package io.swiftwallet.odin.core.services.kafka;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by gibugeorge on 27/12/2016.
 */
public class KafkaClient {

    protected boolean enabled;

    protected List<String> bootstrapServers = new ArrayList<String>(
            Collections.singletonList("localhost:9092"));

    protected String clientId;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(List<String> bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
