/*
 * Copyright 2017 SwiftWallet Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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