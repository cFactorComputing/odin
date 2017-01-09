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

package io.swiftwallet.odin.core.services.server.exception;

import io.swiftwallet.odin.core.exception.OdinException;

/**
 * Created by gibugeorge on 21/12/2016.
 */
public class EmbeddedServerConfigurationException extends OdinException{
    public EmbeddedServerConfigurationException(String message) {
        super(message);
    }

    public EmbeddedServerConfigurationException(String message, Exception e) {
        super(message, e);
    }
}
