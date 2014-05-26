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

import com.tojc.ormlite.android.framework.transaction.controller.TransactionControllerInterfaceBase;
import com.tojc.ormlite.android.framework.transaction.controller.TransactionGeneralControllerInterface;
import com.tojc.ormlite.android.framework.transaction.controller.TransactionThrowableControllerInterface;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by Jaken on 2014/05/25.
 * <p/>
 * NOTE: This class has the potential to change the interface in the future.
 *
 * @since 1.0.5
 */
public abstract class TransactionOrganizerBase implements TransactionOrganizerInterface {
    private Map<TransactionControllerInterfaceBase.ProcessType, TransactionControllerInterfaceBase> controllers = new EnumMap<TransactionControllerInterfaceBase.ProcessType, TransactionControllerInterfaceBase>(TransactionControllerInterfaceBase.ProcessType.class);

    public TransactionOrganizerBase() {
        this.controllers.put(TransactionControllerInterfaceBase.ProcessType.Query, this.createQueryTransactionController());
        this.controllers.put(TransactionControllerInterfaceBase.ProcessType.Insert, this.createInsertTransactionController());
        this.controllers.put(TransactionControllerInterfaceBase.ProcessType.Delete, this.createDeleteTransactionController());
        this.controllers.put(TransactionControllerInterfaceBase.ProcessType.Update, this.createUpdateTransactionController());
        this.controllers.put(TransactionControllerInterfaceBase.ProcessType.BulkInsert, this.createBulkInsertTransactionController());
        this.controllers.put(TransactionControllerInterfaceBase.ProcessType.ApplyBatch, this.createApplyBatchTransactionController());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F extends TransactionControllerInterfaceBase> F getTransactionController(TransactionControllerInterfaceBase.ProcessType processType) {
        return (F) this.controllers.get(processType);
    }

    protected abstract TransactionGeneralControllerInterface createQueryTransactionController();

    protected abstract TransactionGeneralControllerInterface createInsertTransactionController();

    protected abstract TransactionGeneralControllerInterface createDeleteTransactionController();

    protected abstract TransactionGeneralControllerInterface createUpdateTransactionController();

    protected abstract TransactionGeneralControllerInterface createBulkInsertTransactionController();

    protected abstract TransactionThrowableControllerInterface createApplyBatchTransactionController();
}
