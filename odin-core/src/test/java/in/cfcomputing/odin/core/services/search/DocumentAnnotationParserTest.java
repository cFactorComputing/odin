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

import in.cfcomputing.odin.core.services.search.domain.Document;
import in.cfcomputing.odin.core.services.search.domain.GeoSearchable;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DocumentAnnotationParserTest {

    @Test
    public void returnsIndexAndType() {
        final GeoSearchable location = new SomeLocation();
        final DocumentAnnotationParser parser = new DocumentAnnotationParser(location.getClass());
        assertEquals("index", parser.getIndexName());
        assertEquals("type", parser.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionWhenAnnotationIsNotPresent() {
        new DocumentAnnotationParser(new SomeOtherLocation().getClass());
        fail();
    }

    @Document(indexName = "index", type = "type")
    private static class SomeLocation implements GeoSearchable {

        @Override
        public double getLatitude() {
            return 0;
        }

        @Override
        public double getLongitude() {
            return 0;
        }

        @Override
        public double getDistance() {
            return 0;
        }
    }

    private static class SomeOtherLocation {
    }
}