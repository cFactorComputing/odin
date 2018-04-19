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

package in.cfcomputing.odin.core.utils;


import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;

import java.lang.reflect.Field;
import java.util.List;

import static org.apache.commons.lang3.reflect.FieldUtils.getFieldsListWithAnnotation;
import static org.apache.commons.lang3.reflect.FieldUtils.readField;

public class ReflectionUtils extends org.springframework.util.ReflectionUtils {
    private static Object object;
    private static Class annotation;

    private ReflectionUtils() {
    }

    public static Field getFieldWithAnnotation(final Class clazz, final Class annotationClazz) {
        final List<Field> fields = getFieldsListWithAnnotation(clazz, annotationClazz);

        Validate.isTrue(CollectionUtils.isNotEmpty(fields), "No property with annotation [%s] found.",
                annotationClazz.getSimpleName());
        Validate.isTrue(fields.size() == 1, "Multiple fields with [%s] annotation found.",
                annotationClazz.getSimpleName());

        return fields.get(0);
    }

    public static List<Field> getFieldsWithAnnotation(final Class clazz, final Class annotationClazz) {
        final List<Field> fields = getFieldsListWithAnnotation(clazz, annotationClazz);
        Validate.isTrue(CollectionUtils.isNotEmpty(fields), "No property with annotation [%s] found.",
                annotationClazz.getSimpleName());

        return fields;
    }

    public static Object readFieldValue(final Field field, final Object object) {
        try {
            return readField(field, object, true);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Object readFieldValueWithAnnotation(final Object object, final Class annotationClazz) {
        final Field field = getFieldWithAnnotation(object.getClass(), annotationClazz);
        return readFieldValue(field, object);
    }

    public static Object getAnnotationByType(final Object object, Class annotation) {
        return object.getClass().getAnnotation(annotation);
    }
}
