package com.tojc.ormlite.android.event.listenerset;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.tojc.ormlite.android.event.listener.OnDeleteCompletedListener;
import com.tojc.ormlite.android.event.listener.OnDeleteListener;

/**
 * Created by Jaken on 2014/05/10.
 */
public interface DefaultOnDeleteListenerSet<T extends OrmLiteSqliteOpenHelper> extends
        OnDeleteListener<T>,
        OnDeleteCompletedListener<T> {
}
