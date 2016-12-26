package io.swiftwallet.odin.core.bootstrap.cd.event;

import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.springframework.context.ApplicationEvent;

/**
 * Created by gibugeorge on 24/12/2016.
 */
public class ConfigurationChangeEvent extends ApplicationEvent {
    private final transient TreeCacheEvent event;
    private final String eventDesc;

    private final String runtimeContext;

    public ConfigurationChangeEvent(final Object source, final TreeCacheEvent event, final String eventDesc, final String runtimeContext) {
        super(source);
        this.event = event;
        this.eventDesc = eventDesc;
        this.runtimeContext = runtimeContext;
    }

    public TreeCacheEvent getEvent() {
        return event;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public String getRuntimeContext() {
        return runtimeContext;
    }
}
