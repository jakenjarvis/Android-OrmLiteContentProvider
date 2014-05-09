package com.tojc.ormlite.android.framework.event.listenerset;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.tojc.ormlite.android.framework.event.listener.OnInsertCompletedListener;
import com.tojc.ormlite.android.framework.event.listener.OnInsertListener;

/**
 * Created by Jaken on 2014/05/10.
 */
public interface DefaultOnInsertListenerSet<T extends OrmLiteSqliteOpenHelper> extends
        OnInsertListener<T>,
        OnInsertCompletedListener<T> {
}
