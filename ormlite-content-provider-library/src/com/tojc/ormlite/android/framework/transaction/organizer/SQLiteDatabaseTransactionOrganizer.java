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
package com.tojc.ormlite.android.framework.transaction.organizer;

import com.tojc.ormlite.android.framework.transaction.controller.TransactionGeneralControllerInterface;
import com.tojc.ormlite.android.framework.transaction.controller.TransactionThrowableControllerInterface;
import com.tojc.ormlite.android.framework.transaction.controller.base.SQLiteDatabaseTransactionControllerBase;
import com.tojc.ormlite.android.framework.transaction.controller.base.SQLiteDatabaseTransactionThrowableControllerBase;

/**
 * Created by Jaken on 2014/05/25.
 * <p/>
 * NOTE: This class has the potential to change the interface in the future.
 *
 * @since 1.0.5
 */
public class SQLiteDatabaseTransactionOrganizer extends TransactionOrganizerBase {
    public SQLiteDatabaseTransactionOrganizer() {
        super();
    }

    @Override
    protected TransactionGeneralControllerInterface createQueryTransactionController() {
        // Manage the transaction.
        return new SQLiteDatabaseTransactionControllerBase() {
            @Override
            public ProcessType getProcessType() {
                return ProcessType.Query;
            }
        };
    }

    @Override
    protected TransactionGeneralControllerInterface createInsertTransactionController() {
        // Manage the transaction.
        return new SQLiteDatabaseTransactionControllerBase() {
            @Override
            public ProcessType getProcessType() {
                return ProcessType.Insert;
            }
        };
    }

    @Override
    protected TransactionGeneralControllerInterface createDeleteTransactionController() {
        // Manage the transaction.
        return new SQLiteDatabaseTransactionControllerBase() {
            @Override
            public ProcessType getProcessType() {
                return ProcessType.Delete;
            }
        };
    }

    @Override
    protected TransactionGeneralControllerInterface createUpdateTransactionController() {
        // Manage the transaction.
        return new SQLiteDatabaseTransactionControllerBase() {
            @Override
            public ProcessType getProcessType() {
                return ProcessType.Update;
            }
        };
    }

    @Override
    protected TransactionGeneralControllerInterface createBulkInsertTransactionController() {
        // Manage the transaction.
        return new SQLiteDatabaseTransactionControllerBase() {
            @Override
            public ProcessType getProcessType() {
                return ProcessType.BulkInsert;
            }
        };
    }

    @Override
    protected TransactionThrowableControllerInterface createApplyBatchTransactionController() {
        // Manage the transaction.
        return new SQLiteDatabaseTransactionThrowableControllerBase() {
            @Override
            public ProcessType getProcessType() {
                return ProcessType.ApplyBatch;
            }
        };
    }
}
