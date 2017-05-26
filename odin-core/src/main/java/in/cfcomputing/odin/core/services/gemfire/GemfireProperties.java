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

package in.cfcomputing.odin.core.services.gemfire;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by gibugeorge on 12/01/2017.
 */
@ConfigurationProperties(prefix = "gemfire")
public class GemfireProperties {

    private boolean enabled;
    private String locatorHost;
    private int locatorPort;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getLocatorHost() {
        return locatorHost;
    }

    public void setLocatorHost(String locatorHost) {
        this.locatorHost = locatorHost;
    }

    public int getLocatorPort() {
        return locatorPort;
    }

    public void setLocatorPort(int locatorPort) {
        this.locatorPort = locatorPort;
    }
}
