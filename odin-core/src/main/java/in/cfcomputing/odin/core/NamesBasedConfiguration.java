/*
 * Copyright 2018 cFactor Computing Pvt. Ltd.
 *
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

package in.cfcomputing.odin.core;

import in.cfcomputing.odin.core.utils.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;

public interface NamesBasedConfiguration {

    default String[] getNames(final NameBasedProperties nameBasedProperties) {
        final String commaSeparatedDSNames = nameBasedProperties.getNames();
        if (StringUtils.isEmpty(commaSeparatedDSNames)) {
            final ConfigurationProperties annotation = (ConfigurationProperties) ReflectionUtils.getAnnotationByType(nameBasedProperties, ConfigurationProperties.class);
            final String prefix = annotation.prefix();
            throw new ConfigurationException("\"" + prefix + ".names\" property cannot be empty when \"" + prefix + ".enabled=true\".");
        }
        return commaSeparatedDSNames.split(",");
    }

    default String getProperty(final String key, final String name, final Environment environment, final NameBasedProperties nameBasedProperties) {
        final ConfigurationProperties annotation = (ConfigurationProperties) ReflectionUtils.getAnnotationByType(nameBasedProperties, ConfigurationProperties.class);
        final String prefix = annotation.prefix();
        final String actualKey = prefix+"." + name + "." + key;
        return environment.getProperty(actualKey) != null ? environment.getProperty(actualKey) : environment.getProperty(prefix+".default." + key);
    }
}
