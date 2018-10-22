/*
 * Copyright 2018 cFactor Computing Pvt. Ltd.
 *
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

package in.cfcomputing.odin.imdg.config;

import com.hazelcast.core.HazelcastInstance;
import in.cfcomputing.odin.core.bootstrap.config.OdinBootstrapConfiguration;
import in.cfcomputing.odin.jdbc.config.DataSourceConfiguration;
import in.cfcomputing.odin.imdg.ImdgMapStore;
import in.cfcomputing.odin.test.OdinTestUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

public class ImdgPersistenceConfigurationTest {

    @Test
    public void createStoreTables() {
        final String[] envirnoment = new String[]{"imdg.enabled=true",
                "imdg.maps.enabled=true",
                "imdg.maps.names=map1,map2",
                "imdg.maps.map1.time-to-live-seconds=100",
                "imdg.maps.map2.max-idle-seconds=100",
                "imdg.persistence.enabled=true",
                "imdg.persistence.sql-dialect=org.hibernate.dialect.H2Dialect",
                "imdg.persistence.jooq-dialect=H2",
                "imdg.persistence.datasource-name=testDS",
                "jdbc.data-source.enabled=true",
                "jdbc.data-source.names=testDS",
                "jdbc.data-source.testDS.jdbc-url=jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
                "jdbc.data-source.testDS.password=",
                "jdbc.data-source.testDS.user-name=",
                "jdbc.data-source.testDS.cache-prep-stmts=true",
                "jdbc.data-source.testDS.idle-timeout=30000",
                "jdbc.data-source.default.max-life-time=30000",
                "jdbc.data-source.default.maximum-pool-size=5",
                "jdbc.data-source.default.prep-stmt-cache-size=250",
                "jdbc.data-source.default.prep-stmt-cache-sql-limit=100",
                "jdbc.data-source.default.use-server-prep-stmts=true",
                "jdbc.data-source.testDS.driver-class-name=org.h2.Driver"};
        final ApplicationContext applicationContext = OdinTestUtil.
                load(envirnoment, OdinBootstrapConfiguration.class, DataSourceConfiguration.class,
                        ImdgPersistenceConfiguration.class, ImdgConfiguration.class);
        final HazelcastInstance hazelcastInstance = applicationContext.getBean(HazelcastInstance.class);
        Assert.assertNotNull("ImdgMapStore instances should not be null", applicationContext.getBeanNamesForType(ImdgMapStore.class));
        Assert.assertNull(hazelcastInstance.getMap("map1").get("test"));
        hazelcastInstance.getMap("map1").put("test", "test");
        Assert.assertNotNull(hazelcastInstance.getMap("map1").get("test"));

    }
}