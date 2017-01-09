

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

package io.swiftwallet.odin.core.bootstrap.config;

import io.swiftwallet.odin.core.bootstrap.config.annotations.OdinConfiguration;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.Ordered;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;

/**
 * @author gibugeorge on 09/12/16.
 * @version 1.0
 */
public class OdinConfigurationImportSelector implements DeferredImportSelector, BeanClassLoaderAware, Ordered {

    private ClassLoader beanClassLoader;

    @Override
    public String[] selectImports(final AnnotationMetadata importingClassMetadata) {
        final List<String> factoryNames = SpringFactoriesLoader.loadFactoryNames(
                OdinConfiguration.class, this.beanClassLoader);
        return factoryNames.toArray(new String[factoryNames.size()]);
    }

    @Override
    public void setBeanClassLoader(final ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE - 1;
    }
}
