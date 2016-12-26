package io.swiftwallet.odin.core.bootstrap.zk;

import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.springframework.context.ApplicationEvent;

/**
 * Created by gibugeorge on 26/12/2016.
 */
public class ZooKeeperNodeModifiedEvent extends ApplicationEvent {

    private final transient TreeCacheEvent event;
    private final String eventDesc;
    private final String node;

    public ZooKeeperNodeModifiedEvent(final Object source, final TreeCacheEvent event, final String eventDesc, String node) {
        super(source);
        this.event = event;
        this.eventDesc = eventDesc;
        this.node = node;
    }

    public String getNode() {
        return node;
    }

    public TreeCacheEvent getEvent() {
        return event;
    }

    public String getEventDesc() {
        return eventDesc;
    }
}
