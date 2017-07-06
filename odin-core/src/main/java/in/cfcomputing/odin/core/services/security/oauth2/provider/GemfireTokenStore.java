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
            return JsonMapper.fromJson(token.getOAuth2Authentication(), OAuth2Authentication.class);
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
            token.setOAuth2RefreshToken(JsonMapper.toJson(oAuth2AccessToken.getRefreshToken()));
        }
        token.setOAuth2AccessToken(JsonMapper.toJson(oAuth2AccessToken));
        token.setOAuth2Authentication(JsonMapper.toJson(oAuth2Authentication));
        token.setUserName(oAuth2Authentication.isClientOnly() ? StringUtils.EMPTY : oAuth2Authentication.getName());
        token.setClientId(oAuth2Authentication.getOAuth2Request().getClientId());

        final String authenticationKey = keyGenerator.extractKey(oAuth2Authentication);
        token.setAuthenticationKey(authenticationKey);

        tokenCache.delete(token.getToken());
        tokenCache.save(token);

        removeObsolete(token.getUserName());
    }

    private void removeObsolete(final String userName) {
        final Iterable<OAuth2Token> tokenIterable = tokenCache.findByUserName(userName);
        if (tokenIterable != null) {
            tokenIterable.forEach(token -> {
                final OAuth2AccessToken oauthToken = JsonMapper.fromJson(token.getOAuth2AccessToken(), OAuth2AccessToken.class);
                if (oauthToken != null && oauthToken.isExpired()) {
                    tokenCache.delete(token);
                }
            });
        }
    }

    @Override
    public OAuth2AccessToken readAccessToken(final String oauthToken) {
        final OAuth2Token token = tokenCache.findByToken(oauthToken);
        if (token != null) {
            return JsonMapper.fromJson(token.getOAuth2AccessToken(), OAuth2AccessToken.class);
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
            token.setOAuth2RefreshToken(JsonMapper.toJson(oAuth2RefreshToken));
            token.setRefreshTokenAuthentication(JsonMapper.toJson(oAuth2Authentication));
        }
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(final String refreshToken) {
        final OAuth2Token token = tokenCache.findByRefreshToken(refreshToken);
        if (token != null) {
            return JsonMapper.fromJson(token.getOAuth2RefreshToken(), OAuth2RefreshToken.class);
        }
        return null;
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(final OAuth2RefreshToken oAuth2RefreshToken) {
        final OAuth2Token token = tokenCache.findByRefreshToken(oAuth2RefreshToken.getValue());
        if (token != null) {
            return JsonMapper.fromJson(token.getRefreshTokenAuthentication(), OAuth2Authentication.class);
        }
        return null;
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
            tokenCache.delete(token);
        }
    }

    @Override
    public OAuth2AccessToken getAccessToken(final OAuth2Authentication oAuth2Authentication) {
        final String authenticationKey = keyGenerator.extractKey(oAuth2Authentication);
        final OAuth2Token token = tokenCache.findByAuthenticationKey(authenticationKey);
        if (token != null) {
            return JsonMapper.fromJson(token.getOAuth2AccessToken(), OAuth2AccessToken.class);
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
                tokens.add(JsonMapper.fromJson(token.getOAuth2AccessToken(), OAuth2AccessToken.class));
            });
        }
        return tokens;
    }
}
