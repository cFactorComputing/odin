package io.swiftwallet.odin.core.bootstrap.discovery.event;

import io.swiftwallet.odin.core.bootstrap.discovery.config.ServiceDiscoveryConfiguration;
import io.swiftwallet.odin.core.lb.OdinServerRegistry;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

/**
 * Created by gibugeorge on 26/12/2016.
 */
public class ServiceChangeListener {


    @Autowired
    private OdinServerRegistry serverRegistry;

    @Autowired
    private ServiceDiscoveryConfiguration configuration;

    @EventListener
    public void handle(final ServiceChangeEvent serviceChangeEvent) {

        if (serverRegistry != null) {
            final TreeCacheEvent.Type eventType = serviceChangeEvent.getEvent().getType();
            if (eventType == TreeCacheEvent.Type.NODE_ADDED || eventType == TreeCacheEvent.Type.NODE_REMOVED) {
                configuration.register(serverRegistry);
            }
        }
    }
}
