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
package com.tojc.ormlite.android.test.provider;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.tojc.ormlite.android.test.model.Account;
import com.tojc.ormlite.android.test.model.ExtendsAccount;
import com.tojc.ormlite.android.test.model.Membership;

public class SampleHelper extends OrmLiteSqliteOpenHelper {

    /* package-private */static final Class<?>[] CLASS_LIST = new Class<?>[] {Account.class, Membership.class, ExtendsAccount.class};

    public SampleHelper(Context context) {
        super(context, "MyDatabase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            resetAllTables();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("An error occurred in the execution of resetAllTables().", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            resetAllTables();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("An error occurred in the execution of resetAllTables().", e);
        }
    }

    public void resetAllTables() throws SQLException {
        for (Class<?> clazz : CLASS_LIST) {
            TableUtils.dropTable(connectionSource, clazz, true);
            TableUtils.createTable(connectionSource, clazz);
        }
    }
}
