/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2016. All rights reserved.  http://www.nibodha.com
 */

package io.swiftwallet.platform.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main class of the platform which actually starts the application
 */
@SpringBootApplication
public class Platform {
    public static void main(String[] args) {
        SpringApplication application =new SpringApplication(Platform.class);
        application.run(args);
    }
}
