/*
 * Copyright 2018 cFactor Computing Pvt. Ltd.
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

package in.cfcomputing.odin.core.services.imdg;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "imdg")
public class ImdgProperties {

    private boolean enabled;

    private boolean persistenceEnabled;

    private Network network = new Network();

    private Group group = new Group();

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isPersistenceEnabled() {
        return persistenceEnabled;
    }

    public void setPersistenceEnabled(boolean persistenceEnabled) {
        this.persistenceEnabled = persistenceEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public static class Network {
        private int port = 5900;
        private boolean portAutoIncrement = true;

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public boolean isPortAutoIncrement() {
            return portAutoIncrement;
        }

        public void setPortAutoIncrement(boolean portAutoIncrement) {
            this.portAutoIncrement = portAutoIncrement;
        }
    }

    public static class Group {
        private String name = "default";
        private String password = "default";

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }


}
