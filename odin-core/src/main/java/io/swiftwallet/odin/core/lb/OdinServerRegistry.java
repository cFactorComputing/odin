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
