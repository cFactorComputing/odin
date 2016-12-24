package io.swiftwallet.odin.core.lb;

import com.netflix.loadbalancer.Server;

import java.util.UUID;

/**
 * Created by gibugeorge on 18/12/2016.
 */
public class OdinServer extends Server {

    private MetaInfo metaInfo;

    public OdinServer() {
        this(UUID.randomUUID().toString());
    }

    public OdinServer(final String host, final int port) {
        super(host, port);
    }

    public OdinServer(final String id) {
        super(id);
    }

    @Override
    public MetaInfo getMetaInfo() {
        return metaInfo;
    }

    public void setMetaInfo(final MetaInfo metaInfo) {
        this.metaInfo = metaInfo;
    }
}
