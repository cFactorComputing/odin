

package io.swiftwallet.platform.core.bootstrap.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author gibugeorge on 09/12/16.
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(SwiftWalletConfigurationImportSelector.class)
public @interface SwiftWalletConfiguration {
}
