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
package com.tojc.ormlite.android.framework;

import java.lang.reflect.Field;

import com.j256.ormlite.field.DatabaseField;
import com.tojc.ormlite.android.annotation.OrmLiteAnnotationAccessor;
import com.tojc.ormlite.android.annotation.info.ProjectionMapInfo;
import com.tojc.ormlite.android.annotation.info.SortOrderInfo;

/**
 * Manage the database column information.
 * @author Jaken
 */
public class ColumnInfo implements Validity {
    private Field field;
    private String columnName;
    private SortOrderInfo defaultSortOrderInfo;
    private ProjectionMapInfo projectionMapInfo;

    public ColumnInfo(Field columnField) {
        if (!columnField.isAnnotationPresent(DatabaseField.class)) {
            throw new IllegalArgumentException("Parameter does not implement the DatabaseField annotation.");
        }
        if (!(columnField instanceof Field)) {
            throw new IllegalArgumentException("Parameter is not a Field.");
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

        if (this.field == null) {
            result = false;
            if (throwException && !result) {
                throw new IllegalStateException("field is null.");
            }
        } else if (this.columnName.length() <= 0) {
            result = false;
            if (throwException && !result) {
                throw new IllegalStateException("columnName is zero string.");
            }
        }
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
     * @return Gets the default value that is specified in the annotation. This represents the state
     *         of this column only. If you want to know about the table sort order, you refer to the
     *         TableInfo#getDefaultSortOrderString().
     */
    public SortOrderInfo getDefaultSortOrderInfo() {
        return this.defaultSortOrderInfo;
    }

    /**
     * @see com.tojc.ormlite.android.framework.TableInfo#getProjectionMap()
     * @return Gets the default value that is specified in the annotation. This represents the state
     *         of this column only. If you want to know about the table ProjectionMap, you refer to
     *         the TableInfo#getProjectionMap()
     */
    public ProjectionMapInfo getProjectionMapInfo() {
        return this.projectionMapInfo;
    }

}
