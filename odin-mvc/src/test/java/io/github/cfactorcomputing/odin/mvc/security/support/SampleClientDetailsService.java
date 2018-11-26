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

package io.github.cfactorcomputing.odin.mvc.security.support;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by gibugeorge on 06/01/2017.
 */
public class SampleClientDetailsService implements ClientDetailsService {
    @Override
    public ClientDetails loadClientByClientId(final String id) throws ClientRegistrationException {
        final BaseClientDetails details = new BaseClientDetails();
        details.setAccessTokenValiditySeconds(100);
        details.setClientId(id);
        details.setClientSecret("secret");

        final Set<String> scope = new HashSet<>();
        scope.add("read");
        scope.add("write");
        details.setScope(scope);

        return details;
    }
}
