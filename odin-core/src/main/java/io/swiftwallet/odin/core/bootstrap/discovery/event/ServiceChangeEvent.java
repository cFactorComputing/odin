package io.swiftwallet.odin.core.bootstrap.discovery.event;

import io.swiftwallet.odin.core.bootstrap.zk.ZooKeeperNodeModifiedEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;

/**
 * Created by gibugeorge on 26/12/2016.
 */
public class ServiceChangeEvent extends ZooKeeperNodeModifiedEvent {

    public ServiceChangeEvent(final Object source, final TreeCacheEvent event, final String eventDesc, final String node) {
        super(source, event, eventDesc, node);
    }
}
