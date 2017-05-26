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

package in.cfcomputing.odin.core.bootstrap;

import org.springframework.boot.builder.ParentContextApplicationContextInitializer;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;

/**
 * @author gibugeorge on 11/12/16.
 * @version 1.0
 */
public class BootstrapContextInitilizer implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {

    private ConfigurableApplicationContext parentContext;

    public BootstrapContextInitilizer(final ConfigurableApplicationContext parentContext) {
        this.parentContext = parentContext;
    }

    @Override
    public void initialize(final ConfigurableApplicationContext applicationContext) {
        ConfigurableApplicationContext context = null;
        while (applicationContext.getParent() != null && applicationContext.getParent() != applicationContext) {
            context = (ConfigurableApplicationContext) applicationContext.getParent();
        }
        if (context == null) {
            context = applicationContext;
        }
        new ParentContextApplicationContextInitializer(this.parentContext)
                .initialize(context);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 1;
    }

    public void setParentContext(ConfigurableApplicationContext parentContext) {
        this.parentContext = parentContext;
    }
}
