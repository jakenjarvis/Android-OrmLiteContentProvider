package com.tojc.ormlite.android.framework.event.listenerset;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.tojc.ormlite.android.framework.event.listener.OnAfterApplyBatchListener;
import com.tojc.ormlite.android.framework.event.listener.OnBeforeApplyBatchListener;
import com.tojc.ormlite.android.framework.event.listener.OnBulkInsertCompletedListener;
import com.tojc.ormlite.android.framework.event.listener.OnBulkInsertListener;
import com.tojc.ormlite.android.framework.event.listener.OnDeleteCompletedListener;
import com.tojc.ormlite.android.framework.event.listener.OnDeleteListener;
import com.tojc.ormlite.android.framework.event.listener.OnInsertCompletedListener;
import com.tojc.ormlite.android.framework.event.listener.OnInsertListener;
import com.tojc.ormlite.android.framework.event.listener.OnQueryCompletedListener;
import com.tojc.ormlite.android.framework.event.listener.OnQueryListener;
import com.tojc.ormlite.android.framework.event.listener.OnUpdateCompletedListener;
import com.tojc.ormlite.android.framework.event.listener.OnUpdateListener;

/**
 * Created by Jaken on 2014/05/05.
 */
public interface DefaultContentProviderAllListenerSet<T extends OrmLiteSqliteOpenHelper> extends
        OnQueryListener<T>,
        OnQueryCompletedListener<T>,
        OnInsertListener<T>,
        OnInsertCompletedListener<T>,
        OnDeleteListener<T>,
        OnDeleteCompletedListener<T>,
        OnUpdateListener<T>,
        OnUpdateCompletedListener<T>,
        OnBulkInsertListener<T>,
        OnBulkInsertCompletedListener<T>,
        OnBeforeApplyBatchListener<T>,
        OnAfterApplyBatchListener<T> {
}
