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

package io.github.cfactorcomputing.odin.mvc.security.generator;

import io.github.cfactorcomputing.odin.mvc.security.domain.BaseUser;
import io.github.cfactorcomputing.odin.mvc.security.domain.BaseUserRole;
import io.github.cfactorcomputing.odin.mvc.security.oauth2.access.domain.OdinGrantedAuthority;
import io.github.cfactorcomputing.odin.mvc.security.oauth2.access.domain.OdinUserDetails;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

public class UserGenerator<U extends BaseUser<R>, R extends BaseUserRole> {

    public UserDetails generate(final U user) {
        Validate.notNull(user, "BaseUser with id [%s] not found.", user.getUserName());

        final List<R> userRole = user.getRoles();
        Validate.isTrue(CollectionUtils.isNotEmpty(userRole), "BaseUser has no role specified.");

        final OdinUserDetails userDetails = new OdinUserDetails();

        final List<OdinGrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (R role : userRole) {
            final OdinGrantedAuthority grantedAuthority = new OdinGrantedAuthority();
            grantedAuthority.setAuthority(role.getRole().grantedAuthority());
            grantedAuthorities.add(grantedAuthority);
        }

        userDetails.setAuthorities(grantedAuthorities);
        userDetails.setPassword(user.getPassword());
        userDetails.setUsername(user.getUserName());
        userDetails.setAuthenticatedUser(user);

        return userDetails;
    }
}