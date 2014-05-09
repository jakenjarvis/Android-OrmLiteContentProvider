package com.tojc.ormlite.android.framework.event.listener;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.tojc.ormlite.android.framework.MatcherPattern;
import com.tojc.ormlite.android.framework.OperationParameters.InsertParameters;
import com.tojc.ormlite.android.framework.event.listenerbase.ContentProviderEventListenerInterfaceBase;

/**
 * Created by Jaken on 2014/05/05.
 */
public interface OnBulkInsertListener<T extends OrmLiteSqliteOpenHelper> extends ContentProviderEventListenerInterfaceBase {
    /**
     * You implement this method. At the timing of bulkInsert() method, which calls the
     * onBulkInsert(). Start the transaction, will be called for each record.
     *
     * @param helper    This is a helper object. It is the same as one that can be retrieved by
     *                  this.getHelper().
     * @param db        This is a SQLiteDatabase object. Return the object obtained by
     *                  helper.getWritableDatabase().
     * @param target    It is MatcherPattern objects that match to evaluate Uri by UriMatcher. You can
     *                  access information in the tables and columns, ContentUri, MimeType etc.
     * @param parameter Arguments passed to the insert() method.
     * @return Please set value to be returned in the original insert() method.
     * @since 1.0.1
     */
    Uri onBulkInsert(T helper, SQLiteDatabase db, MatcherPattern target, InsertParameters parameter);
}
