package com.tojc.ormlite.android.event.listenerset;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.tojc.ormlite.android.event.listener.OnAfterApplyBatchListener;
import com.tojc.ormlite.android.event.listener.OnAfterBulkInsertListener;
import com.tojc.ormlite.android.event.listener.OnBeforeApplyBatchListener;
import com.tojc.ormlite.android.event.listener.OnBeforeBulkInsertListener;
import com.tojc.ormlite.android.event.listener.OnBulkInsertCompletedListener;
import com.tojc.ormlite.android.event.listener.OnBulkInsertListener;
import com.tojc.ormlite.android.event.listener.OnDeleteCompletedListener;
import com.tojc.ormlite.android.event.listener.OnDeleteListener;
import com.tojc.ormlite.android.event.listener.OnInsertCompletedListener;
import com.tojc.ormlite.android.event.listener.OnInsertListener;
import com.tojc.ormlite.android.event.listener.OnQueryCompletedListener;
import com.tojc.ormlite.android.event.listener.OnQueryListener;
import com.tojc.ormlite.android.event.listener.OnUpdateCompletedListener;
import com.tojc.ormlite.android.event.listener.OnUpdateListener;

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
        OnBeforeBulkInsertListener<T>,
        OnBulkInsertListener<T>,
        OnAfterBulkInsertListener<T>,
        OnBulkInsertCompletedListener<T>,
        OnBeforeApplyBatchListener<T>,
        OnAfterApplyBatchListener<T> {
}
