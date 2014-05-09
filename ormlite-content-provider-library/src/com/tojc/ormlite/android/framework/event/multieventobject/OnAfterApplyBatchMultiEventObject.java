package com.tojc.ormlite.android.framework.event.multieventobject;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.tojc.ormlite.android.framework.event.multieventbase.MultiEventObjectBase;

import java.util.ArrayList;

/**
 * Created by Jaken on 2014/05/06.
 */
public class OnAfterApplyBatchMultiEventObject extends MultiEventObjectBase {
    private final OrmLiteSqliteOpenHelper helper;
    private final SQLiteDatabase db;
    private final ArrayList<ContentProviderOperation> operations;
    private final ContentProviderResult[] result;

    public OnAfterApplyBatchMultiEventObject(Object source, OrmLiteSqliteOpenHelper helper, SQLiteDatabase db, ArrayList<ContentProviderOperation> operations, ContentProviderResult[] result) {
        super(source);
        this.helper = helper;
        this.db = db;
        this.operations = operations;
        this.result = (result != null) ? result.clone() : null; // MEMO: findbugs: EI2
    }

    public OrmLiteSqliteOpenHelper getHelper() {
        return this.helper;
    }

    public SQLiteDatabase getSQLiteDatabase() {
        return this.db;
    }

    public ArrayList<ContentProviderOperation> getOperations() {
        return this.operations;
    }

    public ContentProviderResult[] getResult() {
        return (this.result != null) ? this.result.clone() : null; // MEMO: findbugs: EI2
    }
}
