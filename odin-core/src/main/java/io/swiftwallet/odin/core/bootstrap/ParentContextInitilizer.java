package io.swiftwallet.odin.core.bootstrap;

import org.springframework.boot.builder.ParentContextApplicationContextInitializer;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;

/**
 * @author gibugeorge on 11/12/16.
 * @version 1.0
 */
public class ParentContextInitilizer implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {

    private ConfigurableApplicationContext parentContext;

    public ParentContextInitilizer(final ConfigurableApplicationContext parentContext) {
        this.parentContext = parentContext;
    }

    @Override
    public void initialize(final ConfigurableApplicationContext applicationContext) {
        ConfigurableApplicationContext parentContext = null;
        while (applicationContext.getParent() != null && applicationContext.getParent() != applicationContext) {
            parentContext = (ConfigurableApplicationContext) applicationContext.getParent();
        }
        if (parentContext == null) {
            parentContext = applicationContext;
        }
        new ParentContextApplicationContextInitializer(this.parentContext)
                .initialize(parentContext);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 1;
    }

    public void setParentContext(ConfigurableApplicationContext parentContext) {
        this.parentContext = parentContext;
    }
}
