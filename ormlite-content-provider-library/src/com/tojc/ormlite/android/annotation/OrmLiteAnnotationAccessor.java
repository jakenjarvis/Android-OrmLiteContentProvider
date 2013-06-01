/*
 * This file is part of the Android-OrmLiteContentProvider package.
 *
 * Copyright (c) 2012, Android-OrmLiteContentProvider Team.
 *                     Jaken Jarvis (jaken.jarvis@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * The author may be contacted via
 * https://github.com/jakenjarvis/Android-OrmLiteContentProvider
 */
package com.tojc.ormlite.android.annotation;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTableConfig;

/**
 * Class to access the standard OrmLite annotation.
 * @author Jaken
 */
public final class OrmLiteAnnotationAccessor {

    private OrmLiteAnnotationAccessor() {
        // utility constructor
    }

    /**
     * Gets the table name from DatabaseTable annotation. If the DatabaseTable#tableName is not
     * specified, returns the class name.
     * @param element
     *            Element to be evaluated.
     * @return Returns the table name.
     */
    public static String getAnnotationTableName(AnnotatedElement element) {
        String result = "";
        result = DatabaseTableConfig.extractTableName((Class<?>) element);
        return result;
    }

    /**
     * Gets the column name from DatabaseField annotation. If the DatabaseField#columnName is not
     * specified, returns the field name.
     * @param element
     *            Element to be evaluated.
     * @return Returns the column name.
     */
    public static String getAnnotationColumnName(AnnotatedElement element) {
        String result = "";
        DatabaseField databaseField = element.getAnnotation(DatabaseField.class);
        if (databaseField != null) {
            result = databaseField.columnName();
            if (StringUtils.isEmpty(result)) {
                result = ((Field) element).getName();
            }
        }
        return result;
    }
}
