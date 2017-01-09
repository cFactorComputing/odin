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

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author gibugeorge on 11/12/16.
 * @version 1.0
 */
@ConfigurationProperties(prefix = "configuration-manager")
public class ConfigurationDiscoveryProperties {

    private String root = "configuration";
    private String runtimeConfiguration = "runtime";

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getRuntimeConfiguration() {
        return runtimeConfiguration;
    }

    public void setRuntimeConfiguration(String runtimeConfiguration) {
        this.runtimeConfiguration = runtimeConfiguration;
    }
}
