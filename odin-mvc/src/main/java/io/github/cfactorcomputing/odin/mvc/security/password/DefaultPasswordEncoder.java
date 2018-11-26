/*
 * Copyright (c) 2017 cFactor Computing Pvt. Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
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

package io.github.cfactorcomputing.odin.mvc.security.password;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class DefaultPasswordEncoder implements PasswordEncoder {
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public DefaultPasswordEncoder(@Value("${password.encoder.strength:10}") final int strength) {
        encoder = new BCryptPasswordEncoder(strength, new SecureRandom());
    }

    @Override
    public String encode(final CharSequence value) {
        return encoder.encode(value);
    }

    @Override
    public boolean matches(final CharSequence rawPassword, final String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}
