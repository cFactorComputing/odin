package io.swiftwallet.odin.core.bootstrap.cd.event.listener;

import io.swiftwallet.odin.core.bootstrap.cd.RuntimeConfiguration;
import io.swiftwallet.odin.core.bootstrap.cd.exception.ConfigurationChangeListenerException;
import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import java.nio.charset.Charset;

/**
 * Created by gibugeorge on 24/12/2016.
 */
public class ConfigurationChangeListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationChangeListener.class);
    @Autowired(required = false)
    private RuntimeConfiguration runtimeConfiguration;
    final OgnlContext ctx = new OgnlContext();

    @EventListener
    public void handle(final ConfigurationChangeEvent configurationChangeEvent) {

        if (runtimeConfiguration != null) {
            final String path = configurationChangeEvent.getEvent().getData().getPath();
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Data modified for configuration path {}", path);
            }
            Object expr = null;
            try {
                expr = getOgnlExpression(configurationChangeEvent.getNode(), path);
            } catch (OgnlException e) {
                throw new ConfigurationChangeListenerException("Exception creating ognl expression", e);
            }
            final TreeCacheEvent.Type eventType = configurationChangeEvent.getEvent().getType();
            if ((eventType == TreeCacheEvent.Type.NODE_UPDATED || eventType == TreeCacheEvent.Type.NODE_ADDED) && expr != null) {
                try {
                    Ognl.setValue(expr, ctx, runtimeConfiguration.getConfigurations(), new String(configurationChangeEvent.getEvent().getData().getData(), Charset.defaultCharset()));
                } catch (OgnlException e) {
                    throw new ConfigurationChangeListenerException("Exception setting changed value to runtime configuration", e);
                }
            }
        }
    }

    private static Object getOgnlExpression(final String runtimeContext, String path) throws OgnlException {
        return Ognl.parseExpression(path.replace(runtimeContext + "/", "").replace("/", "."));
    }


}
