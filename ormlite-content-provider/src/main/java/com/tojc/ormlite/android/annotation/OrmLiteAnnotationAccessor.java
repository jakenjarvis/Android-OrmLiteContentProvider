/*
 * This file is part of the Android-OrmLiteContentProvider package.
 * 
 * Copyright (c) 2012, Jaken Jarvis (jaken.jarvis@gmail.com)
 * 
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 * 
 * The author may be contacted via 
 * https://github.com/jakenjarvis/Android-OrmLiteContentProvider
 */
package com.tojc.ormlite.android.annotation;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTableConfig;
//import com.j256.ormlite.table.DatabaseTable;

/**
 * Class to access the standard OrmLite annotation.
 * @author Jaken
 */
public class OrmLiteAnnotationAccessor {
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
            if (result.length() <= 0) {
                result = ((Field) element).getName();
            }
        }
        return result;
    }
}
