package com.tojc.ormlite.android.framework.event.listenerset;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.tojc.ormlite.android.framework.event.listener.OnUpdateCompletedListener;
import com.tojc.ormlite.android.framework.event.listener.OnUpdateListener;

/**
 * Created by Jaken on 2014/05/10.
 */
public interface DefaultOnUpdateListenerSet<T extends OrmLiteSqliteOpenHelper> extends
        OnUpdateListener<T>,
        OnUpdateCompletedListener<T> {
}
