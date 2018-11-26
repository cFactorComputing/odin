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

package io.github.cfactorcomputing.odin.mvc.security.digest.config;

import io.github.cfactorcomputing.odin.mvc.security.OdinSecurityProperties;
import io.github.cfactorcomputing.odin.mvc.security.config.OdinSecurityConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by gibugeorge on 21/12/2016.
 */
@Configuration
@EnableConfigurationProperties(OdinSecurityProperties.class)
public class OdinDigestAuthenticationConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private OdinSecurityProperties odinSecurityProperties;
    @Autowired(required = false)
    private PasswordEncoder passwordEncoder;

    @Bean
    public UserDetailsService userDetailsService() {
        final UserDetails userDetails = new OdinUserDetails();
        return new InMemoryUserDetailsManager(Arrays.asList(userDetails));
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        final DigestAuthenticationEntryPoint entryPoint = new DigestAuthenticationEntryPoint();
        entryPoint.setRealmName(odinSecurityProperties.getDigest().getRealm());
        entryPoint.setKey(odinSecurityProperties.getDigest().getKey());
        final DigestAuthenticationFilter authenticationFilter = new DigestAuthenticationFilter();
        authenticationFilter.setAuthenticationEntryPoint(entryPoint);
        authenticationFilter.setUserDetailsService(userDetailsService());
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests().antMatchers(OdinSecurityConfiguration.DEFAULT_IGNORED).permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic().authenticationEntryPoint(entryPoint)
                .and()
                .addFilterAfter(authenticationFilter, BasicAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        if (passwordEncoder != null) {
            auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder);
        }
        super.configure(auth);
    }

    private class OdinUserDetails implements UserDetails {
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {

            final List<GrantedAuthority> simpleGrantedAuthorityList = new ArrayList<>();
            for (String role : odinSecurityProperties.getUser().getRole()) {
                final GrantedAuthority authority = new SimpleGrantedAuthority(role);
                simpleGrantedAuthorityList.add(authority);
            }
            return simpleGrantedAuthorityList;
        }

        @Override
        public String getPassword() {
            if (passwordEncoder != null) {
                return passwordEncoder.encode(odinSecurityProperties.getUser().getPassword());
            }
            return odinSecurityProperties.getUser().getPassword();
        }

        @Override
        public String getUsername() {
            return odinSecurityProperties.getUser().getName();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}