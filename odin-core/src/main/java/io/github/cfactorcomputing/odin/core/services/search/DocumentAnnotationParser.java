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

package io.github.cfactorcomputing.odin.core.services.search;


import io.github.cfactorcomputing.odin.search.domain.Document;
import org.apache.commons.lang3.Validate;

class DocumentAnnotationParser<T> {
    private final String indexName;
    private final String type;

    DocumentAnnotationParser(final Class<T> clazz) {
        Validate.isTrue(clazz.isAnnotationPresent(Document.class),
                "@Document annotation is missing for the object provided.");

        final Document annotation = clazz.getAnnotation(Document.class);
        this.indexName = annotation.indexName();
        this.type = annotation.type();
    }

    public String getIndexName() {
        return indexName;
    }

    public String getType() {
        return type;
    }
}
