package in.cfcomputing.odin.core.services.security.oauth2.access.domain;

import org.springframework.security.core.GrantedAuthority;

public class OdinGrantedAuthority implements GrantedAuthority{
    private  String authority;

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
