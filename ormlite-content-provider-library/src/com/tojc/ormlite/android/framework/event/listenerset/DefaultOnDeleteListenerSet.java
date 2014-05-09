package com.tojc.ormlite.android.framework.event.listenerset;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.tojc.ormlite.android.framework.event.listener.OnDeleteCompletedListener;
import com.tojc.ormlite.android.framework.event.listener.OnDeleteListener;

/**
 * Created by Jaken on 2014/05/10.
 */
public interface DefaultOnDeleteListenerSet<T extends OrmLiteSqliteOpenHelper> extends
        OnDeleteListener<T>,
        OnDeleteCompletedListener<T> {
}
