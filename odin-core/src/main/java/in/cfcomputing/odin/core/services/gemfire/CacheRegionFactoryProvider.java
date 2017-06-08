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

package in.cfcomputing.odin.core.services.gemfire;

import com.gemstone.gemfire.cache.GemFireCache;
import org.springframework.data.gemfire.client.ClientCacheFactoryBean;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.client.PoolFactoryBean;

public class CacheRegionFactoryProvider {
    private final PoolFactoryBean poolFactoryBean;
    private final ClientCacheFactoryBean cacheFactoryBean;

    public CacheRegionFactoryProvider(final PoolFactoryBean poolFactoryBean,
                                      final ClientCacheFactoryBean cacheFactoryBean) {
        this.poolFactoryBean = poolFactoryBean;
        this.cacheFactoryBean = cacheFactoryBean;
    }

    public ClientRegionFactoryBean provide(final String cacheRegion) throws Exception {
        final ClientRegionFactoryBean cacheRegionFactoryBean = new ClientRegionFactoryBean();
        cacheRegionFactoryBean.setPool(poolFactoryBean.getPool());
        cacheRegionFactoryBean.setCache((GemFireCache) cacheFactoryBean.getObject());
        cacheRegionFactoryBean.setRegionName(cacheRegion);
        return cacheRegionFactoryBean;
    }
}