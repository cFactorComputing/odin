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

import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;

import java.util.Date;

public class OdinOAuth2RefreshToken {
    private String value;
    private Date expiration;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public OAuth2RefreshToken toRefreshToken() {
        return new DefaultExpiringOAuth2RefreshToken(getValue(), getExpiration());
    }

    public static OdinOAuth2RefreshToken fromToken(final OAuth2RefreshToken refreshToken) {
        final OdinOAuth2RefreshToken token = new OdinOAuth2RefreshToken();
        token.setValue(refreshToken.getValue());

        if (refreshToken instanceof DefaultExpiringOAuth2RefreshToken) {
            token.setExpiration(((DefaultExpiringOAuth2RefreshToken) refreshToken).getExpiration());
        }
        return token;
    }
}