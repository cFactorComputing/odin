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

package in.cfcomputing.odin.core.bootstrap.config;

import in.cfcomputing.odin.core.bootstrap.config.annotations.OdinConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by gibugeorge on 30/12/2016.
 */
public class OdinConfigurationImportSelectorTest {

    private final OdinConfigurationImportSelector odinConfigurationImportSelector = new OdinConfigurationImportSelector();

    private final ClassLoader beanClassLoader = new DefaultListableBeanFactory().getBeanClassLoader();

    @Mock
    private AnnotationMetadata annotationMetadata;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.odinConfigurationImportSelector.setBeanClassLoader(beanClassLoader);

    }

    @Test
    public void importsAreSelected() {
        String[] imports = this.odinConfigurationImportSelector.selectImports(this.annotationMetadata);
        assertThat(imports).hasSameSizeAs(SpringFactoriesLoader.loadFactoryNames(
                OdinConfiguration.class, getClass().getClassLoader()));
    }

}