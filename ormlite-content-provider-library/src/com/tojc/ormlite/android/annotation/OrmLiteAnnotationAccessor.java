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
