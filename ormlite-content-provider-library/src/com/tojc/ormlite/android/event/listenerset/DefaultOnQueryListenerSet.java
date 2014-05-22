package com.tojc.ormlite.android.event.listenerset;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.tojc.ormlite.android.event.listener.OnQueryCompletedListener;
import com.tojc.ormlite.android.event.listener.OnQueryListener;

/**
 * Created by Jaken on 2014/05/10.
 */
public interface DefaultOnQueryListenerSet<T extends OrmLiteSqliteOpenHelper> extends
        OnQueryListener<T>,
        OnQueryCompletedListener<T> {
}
