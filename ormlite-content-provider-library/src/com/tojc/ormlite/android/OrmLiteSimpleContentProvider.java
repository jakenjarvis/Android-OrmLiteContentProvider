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
package com.tojc.ormlite.android;

import android.content.ContentUris;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.tojc.ormlite.android.framework.MatcherPattern;
import com.tojc.ormlite.android.framework.OperationParameters.DeleteParameters;
import com.tojc.ormlite.android.framework.OperationParameters.InsertParameters;
import com.tojc.ormlite.android.framework.OperationParameters.QueryParameters;
import com.tojc.ormlite.android.framework.OperationParameters.UpdateParameters;

/**
 * This is a simple class that utilizes the framework. You can make
 * ContentProvider minimal implementation. This is an example of how to
 * implement OrmLiteDefaultContentProvider.
 * @author Jaken
 */
public abstract class OrmLiteSimpleContentProvider<T extends OrmLiteSqliteOpenHelper> extends
    OrmLiteDefaultContentProvider<T> {
    /*
     * @see
     * com.tojc.ormlite.android.OrmLiteDefaultContentProvider#onQuery(com.j256
     * .ormlite.android.apptools .OrmLiteSqliteOpenHelper,
     * com.tojc.ormlite.android.framework.MatcherPattern,
     * com.tojc.ormlite.android.framework.OperationParameters.QueryParameters)
     */
    @Override
    public Cursor onQuery(T helper, SQLiteDatabase db, MatcherPattern target, QueryParameters parameter) {
        Cursor result = null;

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(target.getTableInfo().getName());
        builder.setProjectionMap(target.getTableInfo().getProjectionMap());

        // where
        switch (target.getMimeTypeVnd().getSubType()) {

            case ITEM:
                // Add item selection criteria (using StringBuilder via appendWhere)
                // URI parameter type not set at this level of the class hierarchy, but it is
                // assumed to be numeric so value not escaped.
                builder.appendWhere(target.getTableInfo().getIdColumnInfo().getColumnName());
                builder.appendWhere("=");
                builder.appendWhere(parameter.getUri().getPathSegments().get(1));
                break;

            case DIRECTORY:
            default:
                break;
        }

        // orderBy
        String orderBy = getSortOrderStringForQuery(target, parameter);

        result = builder.query(db, parameter.getProjection(), parameter.getSelection(), parameter.getSelectionArgs(),
            null, null, orderBy);
        return result;
    }

    /*
     * @see
     * com.tojc.ormlite.android.OrmLiteDefaultContentProvider#onInsert(com.j256
     * .ormlite.android. apptools.OrmLiteSqliteOpenHelper,
     * com.tojc.ormlite.android.framework.MatcherPattern,
     * com.tojc.ormlite.android.framework.OperationParameters.InsertParameters)
     */
    @Override
    public Uri onInsert(T helper, SQLiteDatabase db, MatcherPattern target, InsertParameters parameter) {
        Uri result = null;

        long id = db.insert(target.getTableInfo().getName(), null, parameter.getValues());
        if (id >= 0) {
            result = ContentUris.withAppendedId(target.getContentUriPattern(), id);
        } else {
            throw new SQLException("Failed to insert row into : " + parameter.getUri().toString());
        }
        return result;
    }

    /*
     * @see
     * com.tojc.ormlite.android.OrmLiteDefaultContentProvider#onDelete(com.j256
     * .ormlite.android. apptools.OrmLiteSqliteOpenHelper,
     * com.tojc.ormlite.android.framework.MatcherPattern,
     * com.tojc.ormlite.android.framework.OperationParameters.DeleteParameters)
     */
    @Override
    public int onDelete(T helper, SQLiteDatabase db, MatcherPattern target, DeleteParameters parameter) {
        int result = -1;

        switch (target.getMimeTypeVnd().getSubType()) {
            case DIRECTORY:
                result = db.delete(target.getTableInfo().getName(), parameter.getSelection(),
                    parameter.getSelectionArgs());
                break;

            case ITEM:
                String where = target.getTableInfo().getIdColumnInfo().getColumnName() + "="
                    + parameter.getUri().getPathSegments().get(1);
                if (parameter.getSelection() != null && parameter.getSelection().length() >= 1) {
                    where += " AND ( " + parameter.getSelection() + " ) ";
                }
                result = db.delete(target.getTableInfo().getName(), where, parameter.getSelectionArgs());
                break;

            default:
                break;
        }
        return result;
    }

    /*
     * @see
     * com.tojc.ormlite.android.OrmLiteDefaultContentProvider#onUpdate(com.j256
     * .ormlite.android. apptools.OrmLiteSqliteOpenHelper,
     * com.tojc.ormlite.android.framework.MatcherPattern,
     * com.tojc.ormlite.android.framework.OperationParameters.UpdateParameters)
     */
    @Override
    public int onUpdate(T helper, SQLiteDatabase db, MatcherPattern target, UpdateParameters parameter) {
        int result = -1;

        switch (target.getMimeTypeVnd().getSubType()) {
            case DIRECTORY:
                result = db.update(target.getTableInfo().getName(), parameter.getValues(), parameter.getSelection(),
                    parameter.getSelectionArgs());
                break;

            case ITEM:
                String where = target.getTableInfo().getIdColumnInfo().getColumnName() + "="
                    + parameter.getUri().getPathSegments().get(1);
                if (parameter.getSelection() != null && parameter.getSelection().length() >= 1) {
                    where += " AND ( " + parameter.getSelection() + " ) ";
                }
                result = db.update(target.getTableInfo().getName(), parameter.getValues(), where,
                    parameter.getSelectionArgs());
                break;

            default:
                break;
        }
        return result;
    }

    /**
     * This method gets the appropriate sort order.
     * @param target
     *            Arguments passed to the onQuery() method.
     * @param parameter
     *            Arguments passed to the onQuery() method.
     * @return
     * @since 1.0.4
     */
    protected String getSortOrderStringForQuery(MatcherPattern target, QueryParameters parameter) {
        String result = "";
        if (parameter.getSortOrder() != null && parameter.getSortOrder().length() >= 1) {
            result = parameter.getSortOrder();
        } else {
            result = target.getTableInfo().getDefaultSortOrderString();
        }
        return result;
    }

}
