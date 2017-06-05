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

package in.cfcomputing.odin.core.services.search;

import in.cfcomputing.odin.core.services.search.domain.GeoPointField;
import in.cfcomputing.odin.core.services.search.domain.GeoSearchable;
import in.cfcomputing.odin.core.services.search.domain.SearchableField;
import in.cfcomputing.odin.core.utils.JsonMapper;
import org.apache.commons.lang3.Validate;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static in.cfcomputing.odin.core.utils.ReflectionUtils.getFieldWithAnnotation;
import static in.cfcomputing.odin.core.utils.ReflectionUtils.readFieldValue;
import static org.elasticsearch.index.query.QueryBuilders.geoDistanceQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchPhrasePrefixQuery;

@Component
public class ElasticSearchRetriever extends AbstractElasticConnector {

    // TODO temporary fix to get near by search distance from configuration
    @Value("${nearby-search-limit-radius-kilometers:3}")
    private double nearBySearchLimitRadius;

    @Autowired
    public ElasticSearchRetriever(final TransportClientProvider transportClient) {
        super(transportClient);
    }

    public <R> List<R> searchByGeoLocation(final GeoSearchable geoSearchable, final Class<R> responseType) {
        final DocumentAnnotationParser parser = new DocumentAnnotationParser(responseType);
        final String indexName = parser.getIndexName();
        final String type = parser.getType();
        final String geoPointFieldName = getGeopointFieldName(responseType);
        // TODO temporary fix to get near by search distance from configuration
        final Double searchableDistance =
                geoSearchable.getDistance() > 0 ? geoSearchable.getDistance() : nearBySearchLimitRadius;
        Validate.notNull(geoPointFieldName, "Search by geoLocation requires GeoPointField");
        final QueryBuilder qb = geoDistanceQuery(geoPointFieldName)
                .point(geoSearchable.getLatitude(), geoSearchable.getLongitude())
                .geoDistance(GeoDistance.PLANE)
                .distance(searchableDistance, DistanceUnit.KILOMETERS);

        final SearchResponse response = client().prepareSearch(indexName)
                .setTypes(type).setQuery(qb).execute().actionGet();
        final List<R> result = new ArrayList<>();
        if (response != null) {
            for (SearchHit hit : response.getHits()) {
                String sourceAsString = hit.getSourceAsString();
                result.add(JsonMapper.fromJson(sourceAsString, responseType));
            }
        }
        return result;
    }

    private <R> String getGeopointFieldName(Class<R> responseType) {
        return getFieldWithAnnotation(responseType, GeoPointField.class).getName();
    }

    public <R> List<R> search(final Object search, final Class<R> responseType) {
        final DocumentAnnotationParser parser = new DocumentAnnotationParser(responseType);
        final String indexName = parser.getIndexName();
        final String type = parser.getType();

        final Field searchField = getFieldWithAnnotation(search.getClass(), SearchableField.class);
        final Object value = readFieldValue(searchField, search);
        final QueryBuilder qb = matchPhrasePrefixQuery(searchField.getName(), value.toString());

        final SearchResponse response = client().prepareSearch(indexName)
                .setTypes(type).setQuery(qb).execute().actionGet();
        final List<R> result = new ArrayList<>();
        if (response != null) {
            for (SearchHit hit : response.getHits()) {
                String sourceAsString = hit.getSourceAsString();
                result.add(JsonMapper.fromJson(sourceAsString, responseType));
            }
        }
        return result;
    }
}