/*
 * Copyright 2017 SwiftWallet Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"),
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package in.cfcomputing.odin.main;

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
