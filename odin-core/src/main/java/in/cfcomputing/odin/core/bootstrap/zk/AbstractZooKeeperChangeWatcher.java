/*
 * Copyright 2017 SwiftWallet Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"),
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package in.cfcomputing.odin.core.bootstrap.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import javax.annotation.PostConstruct;
import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.apache.curator.framework.recipes.cache.TreeCacheEvent.Type.*;

/**
 * Created by gibugeorge on 26/12/2016.
 */
public abstract class AbstractZooKeeperChangeWatcher implements Closeable, TreeCacheListener, ApplicationEventPublisherAware {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractZooKeeperChangeWatcher.class);
    private AtomicBoolean running = new AtomicBoolean(false);
    private HashMap<String, TreeCache> caches;
    protected String context;

    @Autowired
    protected CuratorFramework curatorFramework;
    protected ApplicationEventPublisher applicationEventPublisher;

    public AbstractZooKeeperChangeWatcher(final String context) {
        this.context = context;
    }

    @PostConstruct
    public void start() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Starting configuration watcher for context " + context);
        }
        if (this.running.compareAndSet(false, true)) {
            this.caches = new HashMap<>();
            if (!context.startsWith("/")) {
                context = "/" + context;
            }
            try {
                final TreeCache cache = TreeCache.newBuilder(this.curatorFramework, context).build();
                cache.getListenable().addListener(this);
                cache.start();
                this.caches.put(context, cache);
            } catch (KeeperException.NoNodeException e) {
                LOGGER.info("Found no node ");
            } catch (Exception e) {
                LOGGER.error("Error initializing listener for context " + context, e);
            }
        }

    }

    @Override
    public void close() throws IOException {
        if (this.running.compareAndSet(true, false)) {
            for (final TreeCache cache : this.caches.values()) {
                cache.close();
            }
            this.caches = null;
        }
    }

    @Override
    public void childEvent(final CuratorFramework curatorFramework, final TreeCacheEvent treeCacheEvent) throws Exception {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("TreeCacheEvent {}", treeCacheEvent);
        }
        final TreeCacheEvent.Type eventType = treeCacheEvent.getType();
        if (eventType == NODE_ADDED || eventType == NODE_REMOVED || eventType == NODE_UPDATED) {
            publish(treeCacheEvent);
        }
    }

    public abstract void publish(final TreeCacheEvent treeCacheEvent);

    @Override
    public void setApplicationEventPublisher(final ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public String getEventDesc(final TreeCacheEvent event) {
        StringBuilder out = new StringBuilder();
        out.append("type=").append(event.getType());
        out.append(", path=").append(event.getData().getPath());
        byte[] data = event.getData().getData();
        if (data != null && data.length > 0) {
            out.append(", data=").append(new String(data, Charset.forName("UTF-8")));
        }
        return out.toString();
    }
}
