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
package in.cfcomputing.odin.core.services.security.oauth2.domain;

import java.io.Serializable;

public class OAuth2Token implements Serializable {
    private static final long serialVersionUID = -5396135693251959177L;

    private String token;
    private String refreshToken;
    private String authenticationKey;
    private String userName;
    private String clientId;

    /*
    * To avoid the dependency to Spring OAuth2 library
    * when storing objects in cache, the composed objects
    * needed for OAuth2 implementation are stored
    * in JSON format. It will be de-serialized only
    * at runtime after retrieving from in-memory grid.
    * */
    private String oAuth2RefreshToken;
    private String oAuth2Authentication;
    private String refreshTokenAuthentication;
    private String oAuth2AccessToken;

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getOAuth2RefreshToken() {
        return oAuth2RefreshToken;
    }

    public void setOAuth2RefreshToken(final String oAuth2RefreshToken) {
        this.oAuth2RefreshToken = oAuth2RefreshToken;
    }

    public String getOAuth2Authentication() {
        return oAuth2Authentication;
    }

    public void setOAuth2Authentication(final String oAuth2Authentication) {
        this.oAuth2Authentication = oAuth2Authentication;
    }

    public String getRefreshTokenAuthentication() {
        return refreshTokenAuthentication;
    }

    public void setRefreshTokenAuthentication(String refreshTokenAuthentication) {
        this.refreshTokenAuthentication = refreshTokenAuthentication;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getOAuth2AccessToken() {
        return oAuth2AccessToken;
    }

    public void setOAuth2AccessToken(final String oAuth2AccessToken) {
        this.oAuth2AccessToken = oAuth2AccessToken;
    }

    public String getAuthenticationKey() {
        return authenticationKey;
    }

    public void setAuthenticationKey(final String authenticationKey) {
        this.authenticationKey = authenticationKey;
    }

    public void removeRefreshToken() {
        refreshToken = null;
        oAuth2RefreshToken = null;
    }
}