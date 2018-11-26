/*
 * Copyright (c) 2017 cFactor Computing Pvt. Ltd.
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

package io.github.cfactorcomputing.odin.mvc.server;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by gibugeorge on 20/12/2016.
 */
@ConfigurationProperties(prefix = "server")
public class EmbeddedServerProperties {

    private int port = 8080;
    private int jmxPort = 1099;
    private String hostName;
    private SslProperties ssl = new SslProperties();

    public int getPort() {
        return port;
    }

    public void setPort(final int port) {
        this.port = port;
    }

    public int getJmxPort() {
        return jmxPort;
    }

    public void setJmxPort(final int jmxPort) {
        this.jmxPort = jmxPort;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public SslProperties getSsl() {
        return ssl;
    }

    public void setSsl(SslProperties ssl) {
        this.ssl = ssl;
    }
}
