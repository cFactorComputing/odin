/*
 * Copyright 2017 SwiftWallet Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"),
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

package io.swiftwallet.odin.core.services.security.config;

import io.swiftwallet.odin.core.bootstrap.config.OdinBootstrapConfiguration;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.DelegatingFilterProxyRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by gibugeorge on 06/02/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigWebContextLoader.class, classes = {OdinBootstrapConfiguration.class, OdinSecurityConfiguration.class})
@EnableWebMvc
@EnableWebSecurity
@WebAppConfiguration
public class OdinSecurityConfigurationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeClass
    public static void setup() {
        System.setProperty("security.user.name", "admin");
        System.setProperty("security.user.password", "admin");
    }

    @Test
    public void testSecurityConfiguration() {
        Assert.assertNotNull(applicationContext.getBean(DelegatingFilterProxyRegistrationBean.class));
        Assert.assertNotNull(applicationContext.getBean(DefaultAuthenticationEventPublisher.class));
    }
}
