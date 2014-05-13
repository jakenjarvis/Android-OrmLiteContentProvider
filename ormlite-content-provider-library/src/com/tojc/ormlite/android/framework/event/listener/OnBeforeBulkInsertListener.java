package com.tojc.ormlite.android.framework.event.listener;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.tojc.ormlite.android.framework.MatcherPattern;
import com.tojc.ormlite.android.framework.event.listenerbase.ContentProviderEventListenerInterfaceBase;

import java.util.List;

/**
 * Created by Jaken on 2014/05/13.
 */
public interface OnBeforeBulkInsertListener<T extends OrmLiteSqliteOpenHelper> extends ContentProviderEventListenerInterfaceBase {
    void onBeforeBulkInsert(T helper, SQLiteDatabase db, MatcherPattern target, Uri uri, List<ContentValues> values);
}
