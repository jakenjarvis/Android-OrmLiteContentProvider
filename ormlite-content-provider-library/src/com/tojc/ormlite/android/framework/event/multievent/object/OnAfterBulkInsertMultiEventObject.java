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
package com.tojc.ormlite.android.framework.event.multievent.object;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.tojc.ormlite.android.framework.MatcherPattern;
import com.tojc.ormlite.android.framework.event.multievent.MultiEventObjectBase;

import java.util.List;

/**
 * Created by Jaken on 2014/05/13.
 */
public class OnAfterBulkInsertMultiEventObject extends MultiEventObjectBase {
    private final OrmLiteSqliteOpenHelper helper;
    private final SQLiteDatabase db;
    private final MatcherPattern matcherPattern;
    private final Uri uri;
    private final List<ContentValues> values;

    public OnAfterBulkInsertMultiEventObject(Object source, OrmLiteSqliteOpenHelper helper, SQLiteDatabase db, MatcherPattern matcherPattern, Uri uri, List<ContentValues> values) {
        super(source);
        this.helper = helper;
        this.db = db;
        this.matcherPattern = matcherPattern;
        this.uri = uri;
        this.values = values;
    }

    public OrmLiteSqliteOpenHelper getHelper() {
        return this.helper;
    }

    public SQLiteDatabase getSQLiteDatabase() {
        return this.db;
    }

    public MatcherPattern getMatcherPattern() {
        return this.matcherPattern;
    }

    public Uri getUri() {
        return this.uri;
    }

    public List<ContentValues> getValues() {
        return this.values;
    }
}
