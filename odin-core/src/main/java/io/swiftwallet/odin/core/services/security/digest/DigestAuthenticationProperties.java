package io.swiftwallet.odin.core.services.security.digest;

import org.springframework.boot.autoconfigure.security.SecurityAuthorizeMode;

/**
 * Created by gibugeorge on 21/12/2016.
 */
public class DigestAuthenticationProperties {

    private String realm = "Odin";
    private SecurityAuthorizeMode authorizeMode = SecurityAuthorizeMode.ROLE;
    private String[] path = new String[]{"/**"};

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public SecurityAuthorizeMode getAuthorizeMode() {
        return authorizeMode;
    }

    public void setAuthorizeMode(SecurityAuthorizeMode authorizeMode) {
        this.authorizeMode = authorizeMode;
    }

    public String[] getPath() {
        return path;
    }

    public void setPath(String[] path) {
        this.path = path;
    }
}
