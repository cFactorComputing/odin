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

package in.cfcomputing.odin.core.test;

import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by gibugeorge on 02/01/2017.
 */
public class OdinTestUtil {

    private OdinTestUtil() {

    }

    public static AnnotationConfigApplicationContext load(String[] environment, Class<?>... clazz) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        EnvironmentTestUtils.addEnvironment(ctx, environment);
        ctx.register(clazz);
        ctx.refresh();
        return ctx;
    }
}
