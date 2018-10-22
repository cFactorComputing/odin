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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class OdinOAuth2Authentication extends AbstractAuthenticationToken {
    private OdinOAuth2Request storedRequest;
    private Authentication userAuthentication;
    private Collection<GrantedAuthority> authorities = new ArrayList<>();
    private Object principal;

    @JsonDeserialize(using = AuthenticationPrincipalDeSerializer.class)
    public void setPrincipal(Object principal) {
        this.principal = principal;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(Collection<OdinGrantedAuthority> authorities) {
        if (authorities != null) {
            for (GrantedAuthority authority : authorities) {
                this.authorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
            }
        }
    }

    public OdinOAuth2Authentication() {
        super(new ArrayList<>());
    }

    public OAuth2Request getOAuth2Request() {
        return storedRequest;
    }

    public void setOAuth2Request(OdinOAuth2Request oAuth2Request) {
        this.storedRequest = oAuth2Request;
    }

    public Authentication getUserAuthentication() {
        return userAuthentication;
    }

    public void setUserAuthentication(OdinOAuth2Authentication userAuthentication) {
        this.userAuthentication = userAuthentication;
    }

    @Override
    public Object getCredentials() {
        return userAuthentication != null ? userAuthentication.getCredentials() : null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    static class AuthenticationPrincipalDeSerializer extends JsonDeserializer<Object> {
        public AuthenticationPrincipalDeSerializer() {
        }

        @Override
        public Object deserialize(final JsonParser jsonParser,
                                  final DeserializationContext deserializationContext) throws IOException {
            if (jsonParser.currentToken().isStructStart()) {
                return jsonParser.readValueAs(OdinUserDetails.class);
            }
            return jsonParser.readValueAs(String.class);
        }
    }
}