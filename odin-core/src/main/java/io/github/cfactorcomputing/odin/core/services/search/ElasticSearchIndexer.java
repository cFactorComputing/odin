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

import org.apache.commons.lang3.Validate;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

import static io.github.cfactorcomputing.odin.core.utils.ReflectionUtils.getFieldWithAnnotation;
import static io.github.cfactorcomputing.odin.core.utils.ReflectionUtils.readFieldValue;
import static io.github.cfactorcomputing.odin.core.utils.JsonMapper.toJson;
import static org.elasticsearch.client.Requests.indicesExistsRequest;

@Component
public class ElasticSearchIndexer extends AbstractElasticConnector {

    @Autowired
    public ElasticSearchIndexer(final TransportClientProvider transportClient) {
        super(transportClient);
    }

    public <T> void createIndex(final T document) {
        final DocumentAnnotationParser parser = new DocumentAnnotationParser(document.getClass());
        final String indexName = parser.getIndexName();
        final String type = parser.getType();

        final Field idField = getFieldWithAnnotation(document.getClass(), Id.class);
        final Object value = readFieldValue(idField, document);
        Validate.notNull(value, "Id field is empty");

        createIndexAndMappingIfNotCreated(document.getClass(), indexName, type, idField.getName());
        addDataToIndex(document, indexName, type, value);
    }

    private <T> void addDataToIndex(T document, String indexName, String type, Object value) {
        client().prepareIndex(indexName, type, value.toString())
                .setSource(toJson(document), XContentType.JSON)
                .get();
    }

    private boolean createIndex(String indexName) {
        return client().admin().indices().prepareCreate(indexName).get().isAcknowledged();
    }

    private <T> boolean createIndexAndMappingIfNotCreated(Class<T> clazz, String indexName, String type, String idFieldName) {
        return indexExists(indexName) || createIndex(indexName) && putMapping(clazz, indexName, type, idFieldName);
    }

    public boolean indexExists(String indexName) {
        return client().admin().indices().exists(indicesExistsRequest(indexName)).actionGet().isExists();
    }

    public <T> boolean putMapping(Class<T> clazz, String indexName, String type, String idFieldName) {
        XContentBuilder xContentBuilder = null;
        try {

            xContentBuilder = MappingBuilder.buildMapping(clazz, type,
                    idFieldName, null);
        } catch (Exception e) {
            throw new ElasticsearchException("Failed to build mapping for " + clazz.getSimpleName(), e);
        }
        return putMapping(indexName, type, xContentBuilder);
    }

    public boolean putMapping(String indexName, String type, Object mapping) {
        PutMappingRequestBuilder requestBuilder = client().admin().indices().preparePutMapping(indexName).setType(type);
        requestBuilder.setSource((XContentBuilder) mapping);
        return requestBuilder.execute().actionGet().isAcknowledged();
    }
}