

package io.swiftwallet.odin.core.bootstrap;

import io.swiftwallet.odin.core.bootstrap.config.OdinBootstrapConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.ClassUtils;

import java.util.*;

/**
 * @author gibugeorge on 11/12/16.
 * @version 1.0
 */
public class BootstrapApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent>, Ordered {

    public static final String BOOTSTRAP_PROPERTIES = "bootstrap";
    private static final Logger LOGGER = LoggerFactory.getLogger(BootstrapApplicationListener.class);

    @Override
    public void onApplicationEvent(final ApplicationEnvironmentPreparedEvent event) {
        final ConfigurableEnvironment environment = event.getEnvironment();
        if (environment.getPropertySources().contains(BOOTSTRAP_PROPERTIES)) {
            return;
        }
        final ConfigurableEnvironment bootstrapEnvironment = new StandardEnvironment();
        final Map<String, Object> bootstrapMap = new HashMap<>();
        bootstrapEnvironment.getPropertySources().addFirst(new MapPropertySource(BOOTSTRAP_PROPERTIES, bootstrapMap));
        final SpringApplicationBuilder bootstrapApplicationBuilder = new SpringApplicationBuilder();
        bootstrapApplicationBuilder.bannerMode(Banner.Mode.OFF)
                .environment(bootstrapEnvironment)
                .properties("spring.application.name:swiftwallet-bootstrap-application")
                .registerShutdownHook(false)
                .logStartupInfo(false)
                .web(false);
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final List<String> configurationClassNames = SpringFactoriesLoader
                .loadFactoryNames(OdinBootstrapConfiguration.class, classLoader);
        final List<Class<?>> bootstrapConfig = new ArrayList<>();
        configurationClassNames.stream().forEach(name -> {
            final Class<?> clazz = ClassUtils.resolveClassName(name, classLoader);
            try {
                clazz.getDeclaredAnnotations();
                bootstrapConfig.add(clazz);
            } catch (Exception e) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Exception while getting declared annotations from {} with exception {}, " +
                            "Skipping addint to kernel config list", clazz, e.getMessage());
                }
            }
        });
        AnnotationAwareOrderComparator.sort(removeDuplicates(bootstrapConfig));
        bootstrapApplicationBuilder.sources(bootstrapConfig.toArray(new Class[bootstrapConfig.size()]));
        final ConfigurableApplicationContext bootstrapContext = bootstrapApplicationBuilder.run();
        addParentContextInitializer(event.getSpringApplication(), bootstrapContext);
        final List<ApplicationContextInitializer> initializers = getOrderedBeansOfType(bootstrapContext,
                ApplicationContextInitializer.class);
        event.getSpringApplication().addInitializers(initializers
                .toArray(new ApplicationContextInitializer[initializers.size()]));

    }

    private static <T> List<T> getOrderedBeansOfType(final ListableBeanFactory context,
                                                     final Class<T> type) {
        final List<T> result = new ArrayList<>();
        for (final String name : context.getBeanNamesForType(type)) {
            result.add(context.getBean(name, type));
        }
        AnnotationAwareOrderComparator.sort(result);
        return result;
    }

    private static void addParentContextInitializer(final SpringApplication application,
                                                    final ConfigurableApplicationContext context) {
        boolean installed = false;
        for (ApplicationContextInitializer<?> initializer : application
                .getInitializers()) {
            if (initializer instanceof BootstrapContextInitilizer) {
                installed = true;
                ((BootstrapContextInitilizer) initializer).setParentContext(context);
            }
        }
        if (!installed) {
            application.addInitializers(new BootstrapContextInitilizer(context));
        }

    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 1;
    }

    protected final <T> List<T> removeDuplicates(List<T> list) {
        return new ArrayList<T>(new LinkedHashSet<T>(list));
    }
}
