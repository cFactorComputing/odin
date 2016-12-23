package io.swiftwallet.odin.core.services.security.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by gibugeorge on 21/12/2016.
 */
@Configuration
@ConditionalOnProperty(prefix = "odin.security", name = "enabled", matchIfMissing = true)
@ImportResource("classpath*:META-INF/spring/digest-auth-context.xml")
public class OdinDigestAuthenticationConfiguration {


}
