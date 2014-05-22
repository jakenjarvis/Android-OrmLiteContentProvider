package com.tojc.ormlite.android.event.listener;

import android.content.ContentProviderOperation;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.tojc.ormlite.android.framework.event.expandevent.ContentProviderEventListenerInterfaceBase;

import java.util.ArrayList;

/**
 * Created by Jaken on 2014/05/05.
 */
public interface OnBeforeApplyBatchListener<T extends OrmLiteSqliteOpenHelper> extends ContentProviderEventListenerInterfaceBase {
    void onBeforeApplyBatch(T helper, SQLiteDatabase db, ArrayList<ContentProviderOperation> operations);
}
