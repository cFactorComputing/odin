

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
