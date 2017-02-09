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

package io.swiftwallet.odin.core.bootstrap.cd;

import io.swiftwallet.odin.core.bootstrap.cd.exception.ConfigurationDiscoveryException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.EnumerablePropertySource;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gibugeorge on 11/12/16.
 * @version 1.0
 */
public class ConfigurationPropertySource extends EnumerablePropertySource<CuratorFramework> {

    private final String context;
    private final String runtimeConfigContext;

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationPropertySource.class);

    private final Map<String, String> properties = new LinkedHashMap<>();
    private RuntimeConfiguration runtimeConfiguration;

    public ConfigurationPropertySource(final String context, final CuratorFramework source, final String runtimeConfigNode) {
        super(context, source);
        this.context = context;
        this.runtimeConfigContext = this.context + "/" + runtimeConfigNode;
        loadProperties(this.context);
        final Object configurationsObject = getHierarchicalConfigurationMap(this.runtimeConfigContext);
        if (configurationsObject instanceof Map) {
            runtimeConfiguration = new RuntimeConfiguration((Map<String, Object>) configurationsObject);
        }
    }

    @Override
    public String[] getPropertyNames() {
        final String[] propertyNames = new String[properties.size()];
        return properties.keySet().toArray(propertyNames);
    }

    @Override
    public Object getProperty(String s) {
        return properties.get(s);
    }

    private void loadProperties(final String context) {
        try {
            log("Entering findProperties for path: {}", context);
            final List<String> children = getChildren(context);
            if (CollectionUtils.isEmpty(children)) {
                log("No properties for path: {}", context);
                return;
            }
            for (final String child : children) {
                String childPath = context + "/" + child;
                if (!childPath.startsWith(this.runtimeConfigContext)) {
                    addProperties(childPath);
                }
            }

            log("Leaving findProperties for path: {}", context);

        } catch (Exception exception) {
            throw new ConfigurationDiscoveryException("Exception loading properties from zookeeper", exception);
        }
    }

    private static void log(final String message, Object... params) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(message, params);
        }
    }

    private void addProperties(final String childPath) {
        byte[] bytes = getPropertyBytes(childPath);
        if (ArrayUtils.isEmpty(bytes)) {
            loadProperties(childPath);
        } else {
            final String key = sanitizeKey(childPath);
            this.properties.put(key, new String(bytes, Charset.forName("UTF-8")));
        }
    }

    private Object getHierarchicalConfigurationMap(final String context) {
        final List<String> children = getChildren(context);

        if (CollectionUtils.isEmpty(children)) {
            byte[] bytes = getPropertyBytes(context);
            return ArrayUtils.isEmpty(bytes) ? "" : new String(bytes, Charset.forName("UTF-8"));
        } else {
            final Map<String, Object> map = new HashMap<>();
            for (final String child : children) {
                map.put(child, getHierarchicalConfigurationMap(context + "/" + child));
            }
            return map;
        }

    }


    private List<String> getChildren(final String context) {
        List<String> children = null;
        try {
            children = this.getSource().getChildren().forPath(context);
        } catch (KeeperException e) {
            if (e.code() != KeeperException.Code.NONODE) { // not found
                throw new ConfigurationDiscoveryException("Exception getting children for path " + context, e);
            }
        } catch (Exception e) {
            throw new ConfigurationDiscoveryException("Exception getting children for path " + context, e);
        }
        return children;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public RuntimeConfiguration getRuntimeConfiguration() {
        return this.runtimeConfiguration;
    }


    private byte[] getPropertyBytes(String fullPath) {
        byte[] bytes = null;
        try {
            bytes = this.getSource().getData().forPath(fullPath);
        } catch (KeeperException e) {
            if (e.code() != KeeperException.Code.NONODE) { // not found
                throw new ConfigurationDiscoveryException("Exception getting data from path " + fullPath, e);
            }
        } catch (Exception e) {
            throw new ConfigurationDiscoveryException("Exception getting data from path " + fullPath, e);
        }
        return bytes;
    }

    private String sanitizeKey(String path) {
        return path.replace(this.context + "/", "").replace('/', '.');
    }
}
