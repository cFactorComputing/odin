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

package io.github.cfactorcomputing.odin.mvc.server.config;

import io.github.cfactorcomputing.odin.core.bootstrap.config.OdinBootstrapConfiguration;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * Created by gibugeorge on 06/01/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {OdinBootstrapConfiguration.class, EmbeddedServerConfiguration.class})
public class EmbeddedServerConfigurationTest {

    @BeforeClass
    public static void beforeClass() {
        System.setProperty("server.port", "8888");
    }

    @Autowired(required = false)
    private JettyServletWebServerFactory embeddedServletContainerFactory;

    @Test
    public void testEmbeddedContainerConfiguration() {
        Assert.assertNotNull(embeddedServletContainerFactory);
//        final EmbeddedServletContainer embeddedServletContainer = embeddedServletContainerFactory.getgetEmbeddedServletContainer();
//        embeddedServletContainer.start();
//        assertEquals(8888, embeddedServletContainer.getPort());
//        embeddedServletContainer.stop();
    }

}