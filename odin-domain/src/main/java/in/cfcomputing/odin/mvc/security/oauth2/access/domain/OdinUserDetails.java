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
package in.cfcomputing.odin.mvc.security.oauth2.access.domain;

import in.cfcomputing.odin.mvc.security.domain.BaseAuthenticatedUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class OdinUserDetails implements UserDetails {
    private String username;
    private String password;
    private Collection<GrantedAuthority> authorities = new ArrayList<>();
    private Object authenticatedUser;

    public <T> T getFromAuthenticatedUser(final String property) {
        if (authenticatedUser instanceof Map) {
            return ((Map<String, T>) authenticatedUser).get(property);
        }
        return null;
    }

    public Object getAuthenticatedUser() {
        return authenticatedUser;
    }

    public void setAuthenticatedUser(Object authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthorities(Collection<OdinGrantedAuthority> authorities) {
        if (authorities != null) {
            for (GrantedAuthority authority : authorities) {
                this.authorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
            }
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getUserId() {
        if (authenticatedUser instanceof BaseAuthenticatedUser) {
            return ((BaseAuthenticatedUser) authenticatedUser).getUserId();
        }
        return null;
    }
}