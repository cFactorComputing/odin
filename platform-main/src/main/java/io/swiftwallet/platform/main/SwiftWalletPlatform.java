/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2016. All rights reserved.  http://www.nibodha.com
 */

package io.swiftwallet.platform.main;

import io.swiftwallet.platform.core.bootstrap.config.SwiftWalletConfiguration;
import org.springframework.boot.SpringApplication;

/**
 * The main class of the platform which actually starts the application
 */
@SwiftWalletConfiguration
public class SwiftWalletPlatform {

    public static void main(String[] args) throws Exception {
        final SpringApplication application = new SpringApplication(SwiftWalletPlatform.class);
        application.run(args);

    }

}
