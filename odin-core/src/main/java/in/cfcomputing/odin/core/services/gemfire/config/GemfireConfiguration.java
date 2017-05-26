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

package in.cfcomputing.odin.core.services.gemfire.config;

import in.cfcomputing.odin.core.services.gemfire.GemfireProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.client.ClientCacheFactoryBean;
import org.springframework.data.gemfire.client.PoolFactoryBean;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.data.gemfire.support.ConnectionEndpoint;

/**
 * Created by gibugeorge on 12/01/2017.
 */
@Configuration
@EnableConfigurationProperties(GemfireProperties.class)
@EnableGemfireRepositories
public class GemfireConfiguration {

    @Autowired
    private GemfireProperties gemfireProperties;

    @Bean
    public ClientCacheFactoryBean cacheFactoryBean(final PoolFactoryBean poolFactoryBean) {
        final ClientCacheFactoryBean clientCacheFactoryBean = new ClientCacheFactoryBean();
        clientCacheFactoryBean.setPool(poolFactoryBean.getPool());
        return clientCacheFactoryBean;
    }

    @Bean
    public PoolFactoryBean poolFactoryBean() {
        final PoolFactoryBean poolFactoryBean = new PoolFactoryBean();
        final ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint(gemfireProperties.getLocatorHost(), gemfireProperties.getLocatorPort());
        poolFactoryBean.addLocators(connectionEndpoint);
        return poolFactoryBean;
    }


}
