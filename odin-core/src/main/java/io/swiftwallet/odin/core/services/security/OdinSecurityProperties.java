package io.swiftwallet.odin.core.services.security;

import io.swiftwallet.odin.core.services.security.digest.DigestAuthenticationProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gibugeorge on 21/12/2016.
 */
@ConfigurationProperties(prefix = "security")
public class OdinSecurityProperties {

    private boolean enabled = true;

    private DigestAuthenticationProperties digest;

    private User user;

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
