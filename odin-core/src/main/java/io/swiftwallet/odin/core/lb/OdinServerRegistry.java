package io.swiftwallet.odin.core.lb;

import org.apache.commons.collections4.map.HashedMap;

import java.util.List;
import java.util.Map;

/**
 * Created by gibugeorge on 24/12/2016.
 */
public class OdinServerRegistry {

    private final Map<String, List<OdinServer>> serverRegistry = new HashedMap<>();

    public void register(final String serviceName, List<OdinServer> serverList) {
        serverRegistry.put(serviceName, serverList);
    }

    public List<OdinServer> lookup(final String serviceName) {
        return serverRegistry.get(serviceName);
    }
}
