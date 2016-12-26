package io.swiftwallet.odin.core.bootstrap.cd.event.listener;

import io.swiftwallet.odin.core.bootstrap.zk.ZooKeeperNodeModifiedEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;

/**
 * Created by gibugeorge on 26/12/2016.
 */
public class ConfigurationChangeEvent extends ZooKeeperNodeModifiedEvent {
    public ConfigurationChangeEvent(Object source, TreeCacheEvent event, String eventDesc, String node) {
        super(source, event, eventDesc, node);
    }
}
