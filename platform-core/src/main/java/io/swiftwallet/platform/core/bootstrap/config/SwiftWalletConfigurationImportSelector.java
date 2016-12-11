/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2016. All rights reserved.  http://www.nibodha.com
 */

package io.swiftwallet.platform.core.bootstrap.config;

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
public class SwiftWalletConfigurationImportSelector implements DeferredImportSelector, BeanClassLoaderAware, Ordered {

    private ClassLoader beanClassLoader;

    @Override
    public String[] selectImports(final AnnotationMetadata importingClassMetadata) {
        final List<String> factoryNames = SpringFactoriesLoader.loadFactoryNames(
                SwiftWalletConfiguration.class, this.beanClassLoader);
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
