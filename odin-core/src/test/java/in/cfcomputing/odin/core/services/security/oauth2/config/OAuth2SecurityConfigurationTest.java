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

package in.cfcomputing.odin.core.services.security.oauth2.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.cfcomputing.odin.core.bootstrap.config.OdinBootstrapConfiguration;
import in.cfcomputing.odin.core.services.security.config.OdinSecurityConfiguration;
import in.cfcomputing.odin.core.services.security.support.AccessTokenWrapper;
import in.cfcomputing.odin.core.services.security.support.SampleClientDetailsService;
import in.cfcomputing.odin.core.services.security.support.SampleUserDetailsService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Collections;
import java.util.Set;

import static junit.framework.TestCase.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by gibugeorge on 06/01/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigWebContextLoader.class, classes = {OAuth2SecurityConfigurationTest.TestConfiguration.class, OdinBootstrapConfiguration.class, OdinSecurityConfiguration.class})
@EnableWebMvc
@EnableWebSecurity
@WebAppConfiguration
public class OAuth2SecurityConfigurationTest {


    @Autowired
    private ObjectMapper objectMapper;
    private final Set<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    private final User user = new User("test", "test", authorities);
    private final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, "test", authorities);

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    @Qualifier("defaultAuthorizationServerTokenServices")
    private ResourceServerTokenServices tokenServices;

    private MockMvc mockMvc;

    @Autowired
    @Qualifier("clientDetailsServiceBean")
    private ClientDetailsService clientDetailsService;

    @BeforeClass
    public static void preSetup() {
        System.setProperty("security.user.name", "user");
        System.setProperty("security.user.password", "test");
        System.setProperty("security.oauth2.enabled", "true");
        System.setProperty("security.oauth2.authorization-server", "true");
        System.setProperty("security.oauth2.resource-server", "true");
    }

    @Before
    public void setup() {
        final OAuth2AuthenticationManager oAuth2Manager = new OAuth2AuthenticationManager();
        oAuth2Manager.setClientDetailsService(clientDetailsService);
        oAuth2Manager.setTokenServices(tokenServices);


        final OAuth2AuthenticationProcessingFilter oAuth2Filter = new OAuth2AuthenticationProcessingFilter();
        oAuth2Filter.setAuthenticationManager(oAuth2Manager);

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilters(oAuth2Filter)
                .build();
    }

    @Test
    public void returnsOAuth2TokenForPasswordGrantType() throws Exception {
        final MvcResult result = this.mockMvc.perform(post("/oauth/token?grant_type=password&username=user&password=test").principal(token).
                header("Authorization", "Basic dGVzdDp0ZXN0").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        verifyAccessToken(result);
    }

    @Test
    public void authorisationFailsForInvalidPassword() throws Exception {
        this.mockMvc.perform(post("/oauth/token?grant_type=password&username=user&password=test1").principal(token).
                header("Authorization", "Basic dGVzdDp0ZXN0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void authorisationFailsForInvalidUser() throws Exception {
        this.mockMvc.perform(post("/oauth/token?grant_type=password&username=user1&password=test").principal(token).
                header("Authorization", "Basic dGVzdDp0ZXN0"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void authorisationFailsWhenPasswordIsMissing() throws Exception {
        this.mockMvc.perform(post("/oauth/token?grant_type=password&username=user&password1=test").principal(token).
                header("Authorization", "Basic dGVzdDp0ZXN0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void returnsOAuth2TokenForClientCredentialsGrantType() throws Exception {
        final MvcResult result = this.mockMvc.perform(post("/oauth/token?grant_type=client_credentials&client_id=test").principal(token).
                header("Authorization", "Basic dGVzdDp0ZXN0").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        verifyAccessToken(result);
    }

    @Test
    public void authorisationFailsForInvalidClientId() throws Exception {
        this.mockMvc.perform(post("/oauth/token?grant_type=client_credentials&client_id=test1").principal(token).
                header("Authorization", "Basic dGVzdDp0ZXN0"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void authorisationFailsForInvalidGrantType() throws Exception {
        this.mockMvc.perform(post("/oauth/token?grant_type=client_credentials1&client_id=test").principal(token)
                .header("Authorization", "Basic dGDzdDp0ZXN0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void authorisationFailsWhenPrincipalIsNotProvided() throws Exception {
        this.mockMvc.perform(post("/oauth/token?grant_type=client_credentials&client_id=test")
                .header("Authorization", "Basic dGDzdDp0ZXN0"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void controllerFailsForInvalidAccessToken() throws Exception {
        this.mockMvc.perform(get(("/test?access_token=111")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void controllerSucceedsForValidAccessToken() throws Exception {
        final MvcResult result = this.mockMvc.perform(post("/oauth/token?grant_type=client_credentials&client_id=test").principal(token)
                .header("Authorization", "Basic dGDzdDp0ZXN0").accept(MediaType.APPLICATION_JSON))
                .andReturn();

        final AccessTokenWrapper token = verifyAccessToken(result);

        this.mockMvc.perform(get(("/test?access_token=" + token.getAccessToken())))
                .andExpect(status().isOk()).andExpect(content().string("success"));
    }

    private AccessTokenWrapper verifyAccessToken(final MvcResult result) throws Exception {
        assertNotNull(result);
        assertNotNull(result.getResponse());
        final String value = result.getResponse().getContentAsString();
        assertNotNull(value);

        final AccessTokenWrapper token = objectMapper.readValue(value, AccessTokenWrapper.class);
        assertNotNull(token);
        assertNotNull(token.getAccessToken());
        return token;
    }

    @ComponentScan("in.cfcomputing.odin.core.services.security.support")
    public static class TestConfiguration {

        @Bean("userDetailsServiceBean")
        public UserDetailsService userDetailsService() {
            return new SampleUserDetailsService();
        }

        @Bean("clientDetailsServiceBean")
        public ClientDetailsService clientDetailsService() {
            return new SampleClientDetailsService();
        }

        @Bean
        public NoOpPasswordEncoder passwordEncoder() {
            return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
        }

    }
}