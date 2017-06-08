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


import com.fasterxml.jackson.databind.ObjectMapper;
import in.cfcomputing.odin.core.services.search.domain.GeoCoordinates;
import in.cfcomputing.odin.core.services.search.domain.GeoSearchable;
import in.cfcomputing.odin.core.services.search.domain.SampleLocation;
import in.cfcomputing.odin.core.services.search.domain.SearchableField;
import org.junit.Assert;

import java.util.List;

//Only for manual verification
public class ElasticSearchConnectorTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AbstractElasticConnector.TransportClientProvider provider;

    public ElasticSearchConnectorTest() throws Exception {
        this.provider = provider();
    }

    private void closeClient() {
        provider.close();
    }

    //@Test
    public void index() throws Exception {
        final GeoCoordinates coordinates = new GeoCoordinates();
        coordinates.setLat(12.0);
        coordinates.setLon(23.0);

        final SampleLocation merchantLocation = new SampleLocation();
        merchantLocation.setName("SwiftWallet");
        merchantLocation.setId("1234");
        merchantLocation.setCoordinates(coordinates);

        final ElasticSearchIndexer indexer = indexer();
        indexer.createIndex(merchantLocation);

        closeClient();
    }

    //@Test
    public void search() throws Exception {
        final SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setBusinessName("Swift");

        final ElasticSearchRetriever retriever = retriever();
        final List<SampleLocation> results = retriever.search(searchCriteria, SampleLocation.class);
        Assert.assertNotNull(results);

        closeClient();
    }

    //@Test
    public void geoSearch() throws Exception {
        final SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setBusinessName("coffee");
        searchCriteria.setLatitude(12.0);
        searchCriteria.setLongitude(23.0);
        searchCriteria.setDistance(10.0);

        final ElasticSearchRetriever retriever = retriever();
        final List<SampleLocation> results = retriever.searchByGeoLocation(searchCriteria, SampleLocation.class);
        Assert.assertNotNull(results);

        closeClient();
    }

    private ElasticSearchIndexer indexer() throws Exception {
        return new ElasticSearchIndexer(this.provider);
    }

    private ElasticSearchRetriever retriever() throws Exception {
        return new ElasticSearchRetriever(this.provider);
    }

    private AbstractElasticConnector.TransportClientProvider provider() throws Exception {
        final TransportClientFactoryBean client = new TransportClientFactoryBean();
        client.setClusterNodes("192.168.43.206:9300");
        client.afterPropertiesSet();
        return client.getObject();
    }


    static class SearchCriteria implements GeoSearchable {
        private double latitude;
        private double longitude;
        @SearchableField
        private String businessName;
        private double distance;

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public String getBusinessName() {
            return businessName;
        }

        public void setBusinessName(String businessName) {
            this.businessName = businessName;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }
    }
}