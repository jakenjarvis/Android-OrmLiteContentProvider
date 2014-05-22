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
package com.tojc.ormlite.android.event.listener;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.tojc.ormlite.android.framework.MatcherPattern;
import com.tojc.ormlite.android.framework.event.expandevent.ContentProviderEventListenerInterfaceBase;

import java.util.List;

/**
 * Created by Jaken on 2014/05/13.
 */
public interface OnBeforeBulkInsertListener<T extends OrmLiteSqliteOpenHelper> extends ContentProviderEventListenerInterfaceBase {
    /**
     * This method is called after opening the transaction and before the onBulkInsert
     * is processing handled.
     * The implementation of the call timing, please refer to the source code of
     * com.tojc.ormlite.android.OrmLiteClassifierContentProvider#internalOnBulkInsert.
     *
     * @param helper This is a helper object. It is the same as one that can be retrieved by
     *               this.getHelper().
     * @param db     This is a SQLiteDatabase object. Return the object obtained by
     *               helper.getWritableDatabase().
     * @param target It is MatcherPattern objects that match to evaluate Uri by UriMatcher. You can
     *               access information in the tables and columns, ContentUri, MimeType etc.
     * @param uri    This is the Uri of target.
     * @param values This is a conversion of the "ContentValues[] values" of android.content.ContentProvider#bulkInsert
     *               to "new ArrayList<ContentValues>(Arrays.asList(values))".
     * @since 1.0.5
     */
    void onBeforeBulkInsert(T helper, SQLiteDatabase db, MatcherPattern target, Uri uri, List<ContentValues> values);
}
