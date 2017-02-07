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

package io.swiftwallet.odin.core.services.security;

import io.swiftwallet.odin.core.services.security.digest.DigestAuthenticationProperties;
import io.swiftwallet.odin.core.services.security.oauth2.OAuth2SecurityProperties;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gibugeorge on 21/12/2016.
 */
@ConfigurationProperties(prefix = "security")
public class OdinSecurityProperties {

    public static final int ACCESS_OVERRIDE_ORDER = SecurityProperties.ACCESS_OVERRIDE_ORDER;
    public static final int BASIC_AUTH_ORDER = SecurityProperties.BASIC_AUTH_ORDER;
    private boolean enabled = true;

    private DigestAuthenticationProperties digest = new DigestAuthenticationProperties();

    private OAuth2SecurityProperties oauth2 = new OAuth2SecurityProperties();

    private User user = new User();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public DigestAuthenticationProperties getDigest() {
        return digest;
    }

    public void setDigest(DigestAuthenticationProperties digest) {
        this.digest = digest;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static class User {

        private String name;
        private String password;
        private List<String> role = new ArrayList<>(Arrays.asList("ROLE_USER"));

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

        public List<String> getRole() {
            return role;
        }

        public void setRole(List<String> role) {
            this.role = role;
        }
    }
}
