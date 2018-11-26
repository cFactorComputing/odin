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
package io.github.cfactorcomputing.odin.mvc.security.oauth2.access.domain;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;

import java.util.Date;
import java.util.Map;
import java.util.Set;

public class OdinOAuth2AccessToken {
    private String value;
    private Date expiration;
    private String tokenType;
    private OdinOAuth2RefreshToken refreshToken;
    private Set<String> scope;
    private Map<String, Object> additionalInformation;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public OdinOAuth2RefreshToken getRefreshToken() {
        return refreshToken;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public void setRefreshToken(OdinOAuth2RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Set<String> getScope() {
        return scope;
    }

    public void setScope(Set<String> scope) {
        this.scope = scope;
    }

    public Map<String, Object> getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(Map<String, Object> additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public OAuth2AccessToken toOAuth2AccessToken() {
        final OAuth2AccessToken token = new OAuth2AccessToken() {
            @Override
            public Map<String, Object> getAdditionalInformation() {
                return additionalInformation;
            }

            @Override
            public Set<String> getScope() {
                return scope;
            }

            @Override
            public OAuth2RefreshToken getRefreshToken() {
                return refreshToken != null ? refreshToken.toRefreshToken() : null;
            }

            @Override
            public String getTokenType() {
                return tokenType;
            }

            @Override
            public boolean isExpired() {
                return false;
            }

            @Override
            public Date getExpiration() {
                return expiration;
            }

            @Override
            public int getExpiresIn() {
                return 0;
            }

            @Override
            public String getValue() {
                return value;
            }
        };

        return new DefaultOAuth2AccessToken(token);
    }

    public static OdinOAuth2AccessToken fromToken(final OAuth2AccessToken oAuth2AccessToken) {
        final OdinOAuth2AccessToken odinOAuth2AccessToken = new OdinOAuth2AccessToken();
        odinOAuth2AccessToken.setAdditionalInformation(oAuth2AccessToken.getAdditionalInformation());
        odinOAuth2AccessToken.setExpiration(oAuth2AccessToken.getExpiration());
        odinOAuth2AccessToken.setRefreshToken(OdinOAuth2RefreshToken.fromToken(oAuth2AccessToken.getRefreshToken()));
        odinOAuth2AccessToken.setScope(oAuth2AccessToken.getScope());
        odinOAuth2AccessToken.setTokenType(oAuth2AccessToken.getTokenType());
        odinOAuth2AccessToken.setValue(oAuth2AccessToken.getValue());

        return odinOAuth2AccessToken;
    }
}