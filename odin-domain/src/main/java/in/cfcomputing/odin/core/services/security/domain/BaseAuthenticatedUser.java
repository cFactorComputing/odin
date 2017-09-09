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

package in.cfcomputing.odin.core.services.security.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Created by gibugeorge on 13/06/2017.
 */
public class BaseAuthenticatedUser<U extends BaseUser> implements Serializable {
    private static final long serialVersionUID = 345678L;
    private String accessToken;
    private U user;
    private String userId;
    private GrantType grantType;
    private Set<String> scope;
    private String refreshToken;
    private Date expiration;

    public Set<String> getScope() {
        return scope;
    }

    public void setScope(Set<String> scope) {
        this.scope = scope;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setUser(final U user) {
        this.user = user;
    }

    public GrantType getGrantType() {
        return grantType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setGrantType(GrantType grantType) {

        this.grantType = grantType;
    }

    public String getUserId() {
        if (user != null) {
            return user.getUserName();
        }
        return userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Date getExpiration() {
        return this.expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public boolean isExpired() {
        return this.expiration != null && this.expiration.before(new Date());
    }

    public U getUser() {
        return user;
    }
}
