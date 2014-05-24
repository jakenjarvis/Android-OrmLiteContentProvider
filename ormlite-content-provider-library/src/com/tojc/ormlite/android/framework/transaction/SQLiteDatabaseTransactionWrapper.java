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
package com.tojc.ormlite.android.framework.transaction;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;

/**
 * Created by Jaken on 2014/05/23.
 * NOTE: This class is incubating and may change in a future version.
 */
public class SQLiteDatabaseTransactionWrapper implements TransactionWrapperInterface {
    private boolean started = false;

    public SQLiteDatabaseTransactionWrapper() {
    }

    @Override
    public <T, E extends Throwable> T transaction(ProcessType processType, OrmLiteSqliteOpenHelper helper, SQLiteDatabase db, OnTransactionThrowableListener<T, E> listener) throws E {
        // TODO: This is not smart.
        T result = null;
        if (!this.hasTransaction()) {
            result = this.internalEnabledTransactionThrowable(db, listener);
        } else {
            result = this.internalDisabledTransactionThrowable(db, listener);
        }
        return result;
    }

    @Override
    public <T> T transaction(ProcessType processType, OrmLiteSqliteOpenHelper helper, SQLiteDatabase db, OnTransactionListener<T> listener) {
        // TODO: This is not smart.
        T result = null;
        if (!this.hasTransaction()) {
            result = this.internalEnabledTransaction(db, listener);
        } else {
            result = this.internalDisabledTransaction(db, listener);
        }
        return result;
    }

    @Override
    public boolean hasTransaction() {
        // TODO: db.inTransaction
        return this.started;
    }

    protected void setStarted(boolean started) {
        this.started = started;
    }

    protected <T, E extends Throwable> T internalEnabledTransactionThrowable(SQLiteDatabase db, OnTransactionThrowableListener<T, E> listener) throws E {
        T result = null;
        db.beginTransaction();
        this.setStarted(true);
        try {
            result = listener.onTransaction();
            if (result != null) {
                db.setTransactionSuccessful();
            }
            listener.onAfterTransactionSuccessful(result);
        } finally {
            db.endTransaction();
            this.setStarted(false);
        }
        return result;
    }

    protected <T, E extends Throwable> T internalDisabledTransactionThrowable(SQLiteDatabase db, OnTransactionThrowableListener<T, E> listener) throws E {
        T result = listener.onTransaction();
        listener.onAfterTransactionSuccessful(result);
        return result;
    }


    protected <T> T internalEnabledTransaction(SQLiteDatabase db, OnTransactionListener<T> listener) {
        T result = null;
        db.beginTransaction();
        this.setStarted(true);
        try {
            result = listener.onTransaction();
            if (result != null) {
                db.setTransactionSuccessful();
            }
            listener.onAfterTransactionSuccessful(result);
        } finally {
            db.endTransaction();
            this.setStarted(false);
        }
        return result;
    }

    protected <T> T internalDisabledTransaction(SQLiteDatabase db, OnTransactionListener<T> listener) {
        T result = listener.onTransaction();
        listener.onAfterTransactionSuccessful(result);
        return result;
    }
}
