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

package in.cfcomputing.odin.core.services.oicollector;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by gibugeorge on 08/02/2017.
 */
@ConfigurationProperties(prefix = "operational-intelligence")
public class OiCollectorProperties {

    private boolean jmxEnabled = true;
    private GraphiteProperties graphite = new GraphiteProperties();

    public boolean isJmxEnabled() {
        return jmxEnabled;
    }

    public void setJmxEnabled(boolean jmxEnabled) {
        this.jmxEnabled = jmxEnabled;
    }

    public GraphiteProperties getGraphite() {
        return graphite;
    }

    public void setGraphite(GraphiteProperties graphite) {
        this.graphite = graphite;
    }
}
