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

package in.cfcomputing.odin.core.services.security.provider;

import in.cfcomputing.odin.core.services.security.domain.GrantType;
import in.cfcomputing.odin.core.services.security.oauth2.access.domain.OdinUserDetails;
import org.apache.commons.lang3.Validate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatedUserProvider {
    public <T> T user() {
        final Object principal = principal();
        if (principal != null) {
            return (T) principal;
        }
        throw new IllegalStateException("Authenticated user not found.");
    }

    public String userId() {
        if (grantType() == GrantType.USER) {
            final User user = user();
            return user.getUsername();
        }
        final OdinUserDetails user = user();
        return user.getUserId();
    }

    private Object principal() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Validate.notNull(authentication, "Authentication is empty");

        return authentication.getPrincipal();
    }

    public GrantType grantType() {
        if (principal() instanceof UserDetails) {
            return GrantType.USER;
        }
        return GrantType.SYSTEM;
    }
}