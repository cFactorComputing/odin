package io.swiftwallet.odin.core.bootstrap.cd;

import io.swiftwallet.odin.core.bootstrap.cd.event.ConfigurationChangeEvent;
import io.swiftwallet.odin.core.bootstrap.zk.AbstractZooKeeperChangeWatcher;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;

/**
 * @author gibugeorge on 11/12/16.
 * @version 1.0
 */
public class ConfigurationChangeWatcher extends AbstractZooKeeperChangeWatcher {


    public ConfigurationChangeWatcher(final String context) {
        super(context);
    }

    @Override
    public void publish(final TreeCacheEvent treeCacheEvent) {
        this.applicationEventPublisher.publishEvent(new ConfigurationChangeEvent(curatorFramework, treeCacheEvent,
                getEventDesc(treeCacheEvent), this.context));
    }
}
