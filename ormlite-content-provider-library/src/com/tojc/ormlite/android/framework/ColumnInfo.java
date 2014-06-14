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

import java.io.Serializable;
import java.lang.reflect.Field;

import com.j256.ormlite.field.DatabaseField;
import com.tojc.ormlite.android.annotation.OrmLiteAnnotationAccessor;
import com.tojc.ormlite.android.annotation.info.ProjectionMapInfo;
import com.tojc.ormlite.android.annotation.info.SortOrderInfo;

/**
 * Manage the database column information.
 * @author Jaken
 */
public class ColumnInfo implements Serializable, Validity {
    private static final long serialVersionUID = -210690551772340412L;

    private transient Field field; // NOTE: findbugs : SE_BAD_FIELD
    private String columnName;
    private SortOrderInfo defaultSortOrderInfo;
    private ProjectionMapInfo projectionMapInfo;

    public ColumnInfo(Field columnField) {
        if (!columnField.isAnnotationPresent(DatabaseField.class)) {
            throw new IllegalArgumentException("Parameter does not implement the DatabaseField annotation.");
        }

        this.field = columnField;
        this.columnName = OrmLiteAnnotationAccessor.getAnnotationColumnName(columnField);
        this.defaultSortOrderInfo = new SortOrderInfo(columnField);
        this.projectionMapInfo = new ProjectionMapInfo(columnField);
    }

    @Override
    public boolean isValid() {
        return isValid(false);
    }

    @Override
    public boolean isValid(boolean throwException) {
        boolean result = true;

        // none of this can happen, keep for a while : May 18th 2013
        // TODO remove if useless
        // if (this.field == null) {
        // result = false;
        // if (throwException && !result) {
        // throw new IllegalStateException("field is null.");
        // }
        // } else if (StringUtils.isEmpty(columnName)) {
        // result = false;
        // if (throwException && !result) {
        // throw new IllegalStateException("columnName is zero string.");
        // }
        // }
        // Acceptable
        // else if(!this.defaultSortOrderInfo.isValid())
        // {
        // result = false;
        // }
        // Acceptable
        // else if(!this.projectionMapInfo.isValid())
        // {
        // result = false;
        // }
        return result;
    }

    public Field getField() {
        return this.field;
    }

    public String getColumnName() {
        return this.columnName;
    }

    public String getProjectionColumnName() {
        String result = this.columnName;
        if (this.projectionMapInfo.isValid()) {
            result = this.projectionMapInfo.getName();
        }
        return result;
    }

    /**
     * @see com.tojc.ormlite.android.framework.TableInfo#getDefaultSortOrderString()
     * @return Gets the default value that is specified in the annotation. This
     *         represents the state of this column only. If you want to know
     *         about the table sort order, you refer to the
     *         TableInfo#getDefaultSortOrderString().
     */
    public SortOrderInfo getDefaultSortOrderInfo() {
        return this.defaultSortOrderInfo;
    }

    /**
     * @see com.tojc.ormlite.android.framework.TableInfo#getProjectionMap()
     * @return Gets the default value that is specified in the annotation. This
     *         represents the state of this column only. If you want to know
     *         about the table ProjectionMap, you refer to the
     *         TableInfo#getProjectionMap()
     */
    public ProjectionMapInfo getProjectionMapInfo() {
        return this.projectionMapInfo;
    }

}
