package io.swiftwallet.odin.main;

import io.swiftwallet.odin.core.bootstrap.config.annotations.OdinConfiguration;
import org.springframework.boot.SpringApplication;

/**
 * The main class of the platform which actually starts the application
 */
@OdinConfiguration
public class OdinMain {

    public static void main(String[] args) throws Exception {
        final SpringApplication application = new SpringApplication(OdinMain.class);
        application.run(args);

    }

}
