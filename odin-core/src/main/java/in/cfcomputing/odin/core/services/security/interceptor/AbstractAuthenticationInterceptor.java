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

package in.cfcomputing.odin.core.services.security.interceptor;

import in.cfcomputing.odin.core.services.security.domain.BaseAuthenticatedUser;
import in.cfcomputing.odin.core.services.security.generator.UserGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.gemfire.repository.GemfireRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

/**
 * Created by gibugeorge on 08/06/2017.
 */
public abstract class AbstractAuthenticationInterceptor<C extends GemfireRepository, U extends BaseAuthenticatedUser> extends HandlerInterceptorAdapter {

    private final C authenticatedUserCache;
    private final UserGenerator userGenerator;

    @Value("${security.enabled:false}")
    private boolean authenticationEnabled;

    public AbstractAuthenticationInterceptor(C authenticatedUserCache, UserGenerator userGenerator) {
        this.authenticatedUserCache = authenticatedUserCache;
        this.userGenerator = userGenerator;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String accessToken = request.getParameter("access_token");
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (StringUtils.isEmpty(accessToken) || authenticationEnabled && authentication == null) {
            throw new AccessDeniedException("Empty access token or Authentication is empty");
        }
        final U user = (U) authenticatedUserCache.findOne(accessToken);
        Validate.notNull(user, "Authenticated user not found in cache.");
        Validate.notNull(user.getAccessToken(), "Access token not found in cache.");
        Validate.isTrue(!user.isExpired(), "Authenticated token expired.");

        final UserDetails userDetails = getUserDetails(user);
        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                userDetails.getPassword(), userDetails.getAuthorities());
        final OAuth2Request oAuth2Request = new OAuth2Request(Collections.emptyMap(), userDetails.getUsername(),
                userDetails.getAuthorities(), true, user.getScope(), Collections.emptySet(),
                StringUtils.EMPTY, Collections.emptySet(), Collections.emptyMap());
        final OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authenticationToken);
        oAuth2Authentication.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(oAuth2Authentication);
        return true;
    }

    public abstract UserDetails getUserDetails(U user);
}
