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

package in.cfcomputing.odin.core.services.server.config;

import in.cfcomputing.odin.core.bootstrap.config.OdinBootstrapConfiguration;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.assertEquals;

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
    private JettyEmbeddedServletContainerFactory embeddedServletContainerFactory;

    @Test
    public void testEmbeddedContainerConfiguration() {
        Assert.assertNotNull(embeddedServletContainerFactory);
        final EmbeddedServletContainer embeddedServletContainer = embeddedServletContainerFactory.getEmbeddedServletContainer();
        embeddedServletContainer.start();
        assertEquals(8888, embeddedServletContainer.getPort());
        embeddedServletContainer.stop();
    }

}