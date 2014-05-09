package com.tojc.ormlite.android.framework.event.listenerset;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.tojc.ormlite.android.framework.event.listener.OnBulkInsertCompletedListener;
import com.tojc.ormlite.android.framework.event.listener.OnBulkInsertListener;

/**
 * Created by Jaken on 2014/05/10.
 */
public interface DefaultOnBulkInsertListenerSet<T extends OrmLiteSqliteOpenHelper> extends
        OnBulkInsertListener<T>,
        OnBulkInsertCompletedListener<T> {
}
