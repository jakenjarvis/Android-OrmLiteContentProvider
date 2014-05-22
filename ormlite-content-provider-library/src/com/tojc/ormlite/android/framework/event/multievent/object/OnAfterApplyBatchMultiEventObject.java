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

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.tojc.ormlite.android.framework.event.multievent.MultiEventObjectBase;

import java.util.ArrayList;

/**
 * Created by Jaken on 2014/05/06.
 */
public class OnAfterApplyBatchMultiEventObject extends MultiEventObjectBase {
    private final OrmLiteSqliteOpenHelper helper;
    private final SQLiteDatabase db;
    private final ArrayList<ContentProviderOperation> operations;
    private final ContentProviderResult[] result;

    public OnAfterApplyBatchMultiEventObject(Object source, OrmLiteSqliteOpenHelper helper, SQLiteDatabase db, ArrayList<ContentProviderOperation> operations, ContentProviderResult[] result) {
        super(source);
        this.helper = helper;
        this.db = db;
        this.operations = operations;
        this.result = (result != null) ? result.clone() : null; // NOTE: findbugs: EI2
    }

    public OrmLiteSqliteOpenHelper getHelper() {
        return this.helper;
    }

    public SQLiteDatabase getSQLiteDatabase() {
        return this.db;
    }

    public ArrayList<ContentProviderOperation> getOperations() {
        return this.operations;
    }

    public ContentProviderResult[] getResult() {
        return (this.result != null) ? this.result.clone() : null; // NOTE: findbugs: EI2
    }
}
