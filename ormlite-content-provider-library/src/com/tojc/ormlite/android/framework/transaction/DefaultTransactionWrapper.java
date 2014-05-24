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
public class DefaultTransactionWrapper extends SQLiteDatabaseTransactionWrapper {
    @Override
    public <T, E extends Throwable> T transaction(TransactionWrapperInterface.ProcessType processType, OrmLiteSqliteOpenHelper helper, SQLiteDatabase db, TransactionWrapperInterface.OnTransactionThrowableListener<T, E> listener) throws E {
        T result = null;
        switch (processType) {
            case Query:
            case Insert:
            case Delete:
            case Update:
                result = this.internalDisabledTransactionThrowable(db, listener);
                break;

            case BulkInsert:
            case ApplyBatch:
                if (!this.hasTransaction()) {
                    result = this.internalEnabledTransactionThrowable(db, listener);
                } else {
                    result = this.internalDisabledTransactionThrowable(db, listener);
                }
                break;

            default:
                throw new IllegalStateException("Process type is invalid.");
        }
        return result;
    }

    @Override
    public <T> T transaction(TransactionWrapperInterface.ProcessType processType, OrmLiteSqliteOpenHelper helper, SQLiteDatabase db, TransactionWrapperInterface.OnTransactionListener<T> listener) {
        T result = null;
        switch (processType) {
            case Query:
            case Insert:
            case Delete:
            case Update:
                result = this.internalDisabledTransaction(db, listener);
                break;

            case BulkInsert:
            case ApplyBatch:
                if (!this.hasTransaction()) {
                    result = this.internalEnabledTransaction(db, listener);
                } else {
                    result = this.internalDisabledTransaction(db, listener);
                }
                break;

            default:
                throw new IllegalStateException("Process type is invalid.");
        }
        return result;
    }
}
