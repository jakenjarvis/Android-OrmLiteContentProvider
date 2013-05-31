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
package com.tojc.ormlite.android.framework;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import android.provider.BaseColumns;

import com.j256.ormlite.field.DatabaseField;
import com.tojc.ormlite.android.annotation.OrmLiteAnnotationAccessor;
import com.tojc.ormlite.android.annotation.info.ContentMimeTypeVndInfo;
import com.tojc.ormlite.android.annotation.info.ContentUriInfo;
import com.tojc.ormlite.android.annotation.info.SortOrderInfo;

/**
 * Manage the database table information.
 * @author Jaken
 */
public class TableInfo implements Validity {
    private Class<?> classType;
    private String name;

    private ContentUriInfo defaultContentUriInfo;
    private ContentMimeTypeVndInfo defaultContentMimeTypeVndInfo;

    private Map<String, ColumnInfo> columns = null;
    private Map<String, String> projectionMap = null;

    private ColumnInfo idColumnInfo = null;

    private String defaultSortOrder = "";

    public TableInfo(Class<?> tableClassType) {
        // can't happen
        // keep a while, May 18th 2013
        // TODO remove after a while
        // if (!(tableClassType instanceof Class<?>)) {
        // throw new IllegalArgumentException("Parameter is not a Class<?>.");
        // }

        this.classType = tableClassType;
        this.name = OrmLiteAnnotationAccessor.getAnnotationTableName(tableClassType);

        this.defaultContentUriInfo = new ContentUriInfo(tableClassType);
        this.defaultContentMimeTypeVndInfo = new ContentMimeTypeVndInfo(tableClassType);

        this.columns = new HashMap<String, ColumnInfo>();
        this.projectionMap = new HashMap<String, String>();

        SortedMap<Integer, String> defaultSortOrderMap = new TreeMap<Integer, String>();

        this.idColumnInfo = null;
        for (Field classfield : tableClassType.getDeclaredFields()) {
            if (classfield.isAnnotationPresent(DatabaseField.class)) {
                classfield.setAccessible(true); // private field accessible

                ColumnInfo columnInfo = new ColumnInfo(classfield);
                this.columns.put(columnInfo.getColumnName(), columnInfo);

                // check id
                if (columnInfo.getColumnName().equals(BaseColumns._ID)) {
                    boolean generatedId = classfield.getAnnotation(DatabaseField.class).generatedId();
                    if (generatedId) {
                        this.idColumnInfo = columnInfo;
                    }
                }

                // DefaultSortOrder
                SortOrderInfo defaultSortOrderInfo = columnInfo.getDefaultSortOrderInfo();
                if (defaultSortOrderInfo.isValid()) {
                    defaultSortOrderMap.put(defaultSortOrderInfo.getWeight(),
                        defaultSortOrderInfo.makeSqlOrderString(columnInfo.getColumnName()));
                }

                // ProjectionMap
                this.projectionMap.put(columnInfo.getProjectionColumnName(), columnInfo.getColumnName());

            }
        }

        if (this.idColumnInfo == null) {
            // @DatabaseField(columnName = _ID, generatedId = true)
            // private int _id;
            throw new IllegalArgumentException("Proper ID is not defined for field.");
        }

        // DefaultSortOrder
        if (defaultSortOrderMap.size() >= 1) {
            // make SQL OrderBy
            StringBuilder result = new StringBuilder();
            String comma = "";
            for (Map.Entry<Integer, String> entry : defaultSortOrderMap.entrySet()) {
                result.append(comma);
                result.append(entry.getValue());
                comma = ", ";
            }
            this.defaultSortOrder = result.toString();
        } else {
            this.defaultSortOrder = "";
        }
    }

    @Override
    public boolean isValid() {
        return isValid(false);
    }

    @Override
    public boolean isValid(boolean throwException) {
        boolean result = true;

        if (this.classType == null) {
            result = false;
            if (throwException && !result) {
                throw new IllegalStateException("classType is null.");
            }
        } else if (StringUtils.isEmpty(name)) {
            result = false;
            if (throwException && !result) {
                throw new IllegalStateException("name is zero string.");
            }
        } else if (this.columns.isEmpty()) {
            result = false;
            if (throwException && !result) {
                throw new IllegalStateException("columns is zero size.");
            }
        } else if (this.columns.size() != this.projectionMap.size()) {
            result = false;
            if (throwException && !result) {
                throw new IllegalStateException("Number of columns and projectionMap do not match.");
            }
        }
        // Acceptable
        // else if(this.defaultSortOrder.length() <= 0)
        // {
        // result = false;
        // }

        // can't happen as columnInfo is always valid
        // keep a while, May 18th 2013
        // TODO remove after a while
        // for (ColumnInfo columnInfo : this.columns.values()) {
        // result = result && columnInfo.isValid(throwException);
        // if (throwException && !result) {
        // throw new IllegalStateException("ColumnInfo " + columnInfo +
        // " is not valid");
        // }
        // }

        return result;
    }

    public Class<?> getClassType() {
        return this.classType;
    }

    public String getName() {
        return this.name;
    }

    /**
     * @see com.tojc.ormlite.android.framework.MatcherPattern#getContentUriInfo()
     * @return Gets the default value that is specified in the annotation. If
     *         you want to know the state to match the pattern, see
     *         MatcherPattern#getContentUriInfo.
     */
    public ContentUriInfo getDefaultContentUriInfo() {
        return this.defaultContentUriInfo;
    }

    /**
     * @see com.tojc.ormlite.android.framework.MatcherController#setDefaultContentUri(String,
     *      String)
     * @see com.tojc.ormlite.android.framework.TableInfo#getDefaultContentUriInfo()
     * @see com.tojc.ormlite.android.framework.MatcherPattern#getContentUriInfo()
     * @param defaultContentUriInfo
     */
    public void setDefaultContentUriInfo(ContentUriInfo defaultContentUriInfo) {
        this.defaultContentUriInfo = defaultContentUriInfo;
    }

    /**
     * @see com.tojc.ormlite.android.framework.MatcherPattern#getMimeTypeVnd()
     * @return Gets the default value that is specified in the annotation. If
     *         you want to know the state to match the pattern, see
     *         MatcherPattern#getMimeTypeVnd.
     */
    public ContentMimeTypeVndInfo getDefaultContentMimeTypeVndInfo() {
        return this.defaultContentMimeTypeVndInfo;
    }

    /**
     * @see com.tojc.ormlite.android.framework.MatcherController#setDefaultContentMimeTypeVnd(String,
     *      String)
     * @see com.tojc.ormlite.android.framework.TableInfo#getDefaultContentMimeTypeVndInfo()
     * @see com.tojc.ormlite.android.framework.MatcherPattern#getMimeTypeVnd()
     * @param defaultContentMimeTypeVndInfo
     */
    public void setDefaultContentMimeTypeVndInfo(ContentMimeTypeVndInfo defaultContentMimeTypeVndInfo) {
        this.defaultContentMimeTypeVndInfo = defaultContentMimeTypeVndInfo;
    }

    /**
     * @return Gets the default value that is specified in the annotation. If
     *         you are specifying multiple fields, return the concatenated
     *         string. ex) "timestamp DESC, _id ASC"
     */
    public String getDefaultSortOrderString() {
        return this.defaultSortOrder;
    }

    /**
     * @return Get column information for column "_id".
     */
    public ColumnInfo getIdColumnInfo() {
        return this.idColumnInfo;
    }

    /**
     * @see com.tojc.ormlite.android.annotation.AdditionalAnnotation.ProjectionMap
     * @return Return to generate the ProjectionMap. If you are not using the
     *         ProjectionMap annotations need not be used. The Map is includes
     *         all columns.
     */
    public Map<String, String> getProjectionMap() {
        return this.projectionMap;
    }

}
