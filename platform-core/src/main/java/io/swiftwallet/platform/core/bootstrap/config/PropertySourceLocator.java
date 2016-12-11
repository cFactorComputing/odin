package io.swiftwallet.platform.core.bootstrap.config;

import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;

/**
 * @author gibugeorge on 11/12/16.
 * @version 1.0
 */
@FunctionalInterface
public interface PropertySourceLocator {

    PropertySource<?> locate(Environment environment);
}
