package io.swiftwallet.odin.core.lb.event;

import org.springframework.context.ApplicationEvent;

/**
 * Created by gibugeorge on 30/03/2017.
 */
public class ServerListUpdatedEvent extends ApplicationEvent {

    private final String serviceName;

    public ServerListUpdatedEvent(Object serviceName) {
        super(serviceName);
        this.serviceName = (String) serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }
}
