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
import org.springframework.data.gemfire.repository.GemfireRepository;

import java.io.Serializable;

public interface TokenCache<T extends OAuth2Token, ID extends Serializable> extends GemfireRepository<T, ID> {

    T findByToken(String oauthToken);

    T findByRefreshToken(String value);

    Iterable<T> findByAuthenticationKey(String authenticationKey);

    Iterable<T> findByClientIdAndUserName(String clientId, String userName);

    Iterable<T> findByClientId(String clientId);

    Iterable<T> findByUserName(String userName);
}