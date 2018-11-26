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

import org.springframework.security.oauth2.provider.OAuth2Request;

public class OdinOAuth2Request extends OAuth2Request {

    /**
     * Empty method to avoid JSON serialization issue with OAuth2Request.
     */
    public void setRefresh(boolean refresh) {
    }
}