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

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.tojc.ormlite.android.framework.MatcherPattern;
import com.tojc.ormlite.android.framework.OperationParameters.DeleteParameters;
import com.tojc.ormlite.android.framework.OperationParameters.InsertParameters;
import com.tojc.ormlite.android.framework.OperationParameters.QueryParameters;
import com.tojc.ormlite.android.framework.OperationParameters.UpdateParameters;

import java.util.ArrayList;

/**
 * To take advantage of the framework, it provides a standard class. If you use this library, you
 * should inherit from this class (or a provided subclass).
 *
 * @author Jaken
 */
public abstract class OrmLiteDefaultContentProvider<T extends OrmLiteSqliteOpenHelper> extends OrmLiteClassifierContentProvider<T> {
    @Override
    public void onQueryCompleted(Cursor result, Uri uri, MatcherPattern target, QueryParameters parameter) {
        result.setNotificationUri(this.getContext().getContentResolver(), uri);
    }

    @Override
    public void onInsertCompleted(Uri result, Uri uri, MatcherPattern target, InsertParameters parameter) {
        this.getContext().getContentResolver().notifyChange(result, null);
    }

    @Override
    public void onDeleteCompleted(int result, Uri uri, MatcherPattern target, DeleteParameters parameter) {
        this.getContext().getContentResolver().notifyChange(uri, null);
    }

    @Override
    public void onUpdateCompleted(int result, Uri uri, MatcherPattern target, UpdateParameters parameter) {
        this.getContext().getContentResolver().notifyChange(uri, null);
    }

    @Override
    public Uri onBulkInsert(T helper, SQLiteDatabase db, MatcherPattern target, InsertParameters parameter) {
        return onInsert(helper, db, target, parameter);
    }

    @Override
    public void onBulkInsertCompleted(int result, Uri uri) {
        this.getContext().getContentResolver().notifyChange(uri, null);
    }

    @Override
    public void onBeforeApplyBatch(T helper, SQLiteDatabase db, ArrayList<ContentProviderOperation> operations) {
    }

    @Override
    public void onAfterApplyBatch(T helper, SQLiteDatabase db, ArrayList<ContentProviderOperation> operations, ContentProviderResult[] result) {
    }

    /**
     * This method gets the appropriate sort order.
     *
     * @param target    Arguments passed to the onQuery() method.
     * @param parameter Arguments passed to the onQuery() method.
     * @return return an sort order string.
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
