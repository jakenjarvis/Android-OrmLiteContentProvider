package com.tojc.ormlite.android.framework.event.listenerset;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.tojc.ormlite.android.framework.event.listener.OnAfterApplyBatchListener;
import com.tojc.ormlite.android.framework.event.listener.OnBeforeApplyBatchListener;

/**
 * Created by Jaken on 2014/05/10.
 */
public interface DefaultOnApplyBatchListenerSet<T extends OrmLiteSqliteOpenHelper> extends
        OnBeforeApplyBatchListener<T>,
        OnAfterApplyBatchListener<T> {
}
