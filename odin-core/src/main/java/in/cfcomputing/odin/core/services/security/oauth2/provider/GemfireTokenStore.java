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
package in.cfcomputing.odin.core.services.security.oauth2.provider;

import in.cfcomputing.odin.core.services.security.oauth2.access.domain.OdinOAuth2AccessToken;
import in.cfcomputing.odin.core.services.security.oauth2.access.domain.OdinOAuth2Authentication;
import in.cfcomputing.odin.core.services.security.oauth2.access.domain.OdinOAuth2RefreshToken;
import in.cfcomputing.odin.core.services.security.oauth2.domain.OAuth2Token;
import in.cfcomputing.odin.core.utils.JsonMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class GemfireTokenStore<C extends TokenCache> implements TokenStore {
    private final C tokenCache;
    private final AuthenticationKeyGenerator keyGenerator;

    public GemfireTokenStore(final C tokenCache) {
        this.keyGenerator = new DefaultAuthenticationKeyGenerator();
        this.tokenCache = tokenCache;
    }

    @Override
    public OAuth2Authentication readAuthentication(final OAuth2AccessToken oAuth2AccessToken) {
        return readAuthentication(oAuth2AccessToken.getValue());
    }

    @Override
    public OAuth2Authentication readAuthentication(final String oauthToken) {
        final OAuth2Token token = tokenCache.findByToken(oauthToken);
        if (token != null) {
            return toOAuth2Authentication(token.getOAuth2Authentication());
        }
        return null;
    }

    protected abstract OAuth2Token createNewToken();

    @Override
    public void storeAccessToken(final OAuth2AccessToken oAuth2AccessToken, final OAuth2Authentication oAuth2Authentication) {
        final OAuth2Token token = createNewToken();

        token.setToken(oAuth2AccessToken.getValue());
        if (oAuth2AccessToken.getRefreshToken() != null) {
            token.setRefreshToken(oAuth2AccessToken.getRefreshToken().getValue());
            token.setOAuth2RefreshToken(JsonMapper.toJson(OdinOAuth2RefreshToken.fromToken(oAuth2AccessToken.getRefreshToken())));
        }
        token.setOAuth2AccessToken(JsonMapper.toJson(OdinOAuth2AccessToken.fromToken(oAuth2AccessToken)));
        token.setOAuth2Authentication(JsonMapper.toJson(oAuth2Authentication));
        token.setUserName(oAuth2Authentication.isClientOnly() ? StringUtils.EMPTY : oAuth2Authentication.getName());
        token.setClientId(oAuth2Authentication.getOAuth2Request().getClientId());

        final String authenticationKey = keyGenerator.extractKey(oAuth2Authentication);
        token.setAuthenticationKey(authenticationKey);

        tokenCache.save(token);
    }

    @Override
    public OAuth2AccessToken readAccessToken(final String oauthToken) {
        final OAuth2Token token = tokenCache.findByToken(oauthToken);
        if (token != null) {
            final OdinOAuth2AccessToken auth2AccessToken =
                    JsonMapper.fromJson(token.getOAuth2AccessToken(), OdinOAuth2AccessToken.class);
            return auth2AccessToken.toOAuth2AccessToken();
        }
        return null;
    }

    @Override
    public void removeAccessToken(final OAuth2AccessToken oAuth2AccessToken) {
        tokenCache.delete(oAuth2AccessToken.getValue());
    }

    @Override
    public void storeRefreshToken(final OAuth2RefreshToken oAuth2RefreshToken, final OAuth2Authentication oAuth2Authentication) {
        final OAuth2Token token = tokenCache.findByRefreshToken(oAuth2RefreshToken.getValue());
        if (token != null) {
            token.setOAuth2RefreshToken(JsonMapper.toJson(OdinOAuth2RefreshToken.fromToken(oAuth2RefreshToken)));
            token.setRefreshTokenAuthentication(JsonMapper.toJson(oAuth2Authentication));
            tokenCache.save(token);
        }
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(final String refreshToken) {
        final OAuth2Token token = tokenCache.findByRefreshToken(refreshToken);
        if (token != null) {
            final OdinOAuth2RefreshToken auth2RefreshToken =
                    JsonMapper.fromJson(token.getOAuth2RefreshToken(), OdinOAuth2RefreshToken.class);
            return auth2RefreshToken.toRefreshToken();
        }
        return null;
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(final OAuth2RefreshToken oAuth2RefreshToken) {
        final OAuth2Token token = tokenCache.findByRefreshToken(oAuth2RefreshToken.getValue());
        if (token != null) {
            return toOAuth2Authentication(token.getRefreshTokenAuthentication());
        }
        return null;
    }

    private OAuth2Authentication toOAuth2Authentication(final String value) {
        final OdinOAuth2Authentication auth2Authentication =
                JsonMapper.fromJson(value, OdinOAuth2Authentication.class);
        return new OAuth2Authentication(auth2Authentication.getOAuth2Request(), auth2Authentication.getUserAuthentication());
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken oAuth2RefreshToken) {
        final OAuth2Token token = tokenCache.findByRefreshToken(oAuth2RefreshToken.getValue());
        if (token != null) {
            token.removeRefreshToken();
            tokenCache.save(token);
        }
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(final OAuth2RefreshToken oAuth2RefreshToken) {
        final OAuth2Token token = tokenCache.findByRefreshToken(oAuth2RefreshToken.getValue());
        if (token != null) {
            tokenCache.delete(token.getToken());
        }
    }

    @Override
    public OAuth2AccessToken getAccessToken(final OAuth2Authentication oAuth2Authentication) {
        final String authenticationKey = keyGenerator.extractKey(oAuth2Authentication);
        final Iterable<OAuth2Token> tokens = tokenCache.findByAuthenticationKey(authenticationKey);
        if (tokens != null) {
            for (OAuth2Token token : tokens) {
                final OdinOAuth2AccessToken auth2AccessToken =
                        JsonMapper.fromJson(token.getOAuth2AccessToken(), OdinOAuth2AccessToken.class);
                return auth2AccessToken.toOAuth2AccessToken();
            }
        }
        return null;
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(final String clientId, final String userName) {
        final Iterable<OAuth2Token> tokenIterable = tokenCache.findByClientIdAndUserName(clientId, userName);
        return toOAuth2AccessTokens(tokenIterable);
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(final String clientId) {
        final Iterable<OAuth2Token> tokenIterable = tokenCache.findByClientId(clientId);
        return toOAuth2AccessTokens(tokenIterable);
    }

    private Collection<OAuth2AccessToken> toOAuth2AccessTokens(final Iterable<OAuth2Token> tokenIterable) {
        final List<OAuth2AccessToken> tokens = new ArrayList<>();
        if (tokenIterable != null) {
            tokenIterable.forEach(token -> {
                final OdinOAuth2AccessToken auth2AccessToken =
                        JsonMapper.fromJson(token.getOAuth2AccessToken(), OdinOAuth2AccessToken.class);
                tokens.add(auth2AccessToken.toOAuth2AccessToken());
            });
        }
        return tokens;
    }
}