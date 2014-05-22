package com.tojc.ormlite.android.event.listener;

import android.net.Uri;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.tojc.ormlite.android.framework.event.expandevent.ContentProviderEventListenerInterfaceBase;

/**
 * Created by Jaken on 2014/05/05.
 */
public interface OnBulkInsertCompletedListener<T extends OrmLiteSqliteOpenHelper> extends ContentProviderEventListenerInterfaceBase {
    /**
     * This method is called after the bulkInsert processing has been handled. If you're a need,
     * you can override this method.
     *
     * @param result This is the return value of bulkInsert method.
     * @param uri    This is the Uri of target.
     * @since 1.0.4
     */
    void onBulkInsertCompleted(int result, Uri uri);
}
