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

package io.github.cfactorcomputing.odin.mvc.security.oauth2;

/**
 * Created by gibugeorge on 29/12/2016.
 */
public class OAuth2SecurityProperties {

    private boolean enabled;

    private boolean resourceServer;

    private boolean authorizationServer;


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isResourceServer() {
        return resourceServer;
    }

    public void setResourceServer(boolean resourceServer) {
        this.resourceServer = resourceServer;
    }

    public boolean isAuthorizationServer() {
        return authorizationServer;
    }

    public void setAuthorizationServer(boolean authorizationServer) {
        this.authorizationServer = authorizationServer;
    }
}
