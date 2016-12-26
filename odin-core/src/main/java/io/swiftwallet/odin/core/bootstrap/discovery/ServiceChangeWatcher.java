package io.swiftwallet.odin.core.bootstrap.discovery;

import io.swiftwallet.odin.core.bootstrap.discovery.event.ServiceChangeEvent;
import io.swiftwallet.odin.core.bootstrap.zk.AbstractZooKeeperChangeWatcher;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;

/**
 * Created by gibugeorge on 26/12/2016.
 */
public class ServiceChangeWatcher extends AbstractZooKeeperChangeWatcher {


    public ServiceChangeWatcher(String context) {
        super(context);
    }

    @Override
    public void publish(TreeCacheEvent treeCacheEvent) {
        applicationEventPublisher.publishEvent(new ServiceChangeEvent(curatorFramework, treeCacheEvent,
                getEventDesc(treeCacheEvent), this.context));
    }
}
