package com.tojc.ormlite.android.framework.event.multieventobject;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.tojc.ormlite.android.framework.MatcherPattern;
import com.tojc.ormlite.android.framework.OperationParameters;
import com.tojc.ormlite.android.framework.Parameter;
import com.tojc.ormlite.android.framework.event.multieventbase.MultiEventObjectBase;

/**
 * Created by Jaken on 2014/05/06.
 */
public class OnInsertMultiEventObject extends MultiEventObjectBase {
    private final OrmLiteSqliteOpenHelper helper;
    private final SQLiteDatabase db;
    private final MatcherPattern matcherPattern;
    private final Parameter parameter;

    private Uri returnValue;

    public OnInsertMultiEventObject(Object source, OrmLiteSqliteOpenHelper helper, SQLiteDatabase db, MatcherPattern matcherPattern, Parameter parameter) {
        super(source);
        this.helper = helper;
        this.db = db;
        this.matcherPattern = matcherPattern;
        this.parameter = parameter;

        this.returnValue = null;
    }

    public OrmLiteSqliteOpenHelper getHelper() {
        return this.helper;
    }

    public SQLiteDatabase getSQLiteDatabase() {
        return this.db;
    }

    public MatcherPattern getMatcherPattern() {
        return this.matcherPattern;
    }

    public OperationParameters.InsertParameters getParameter() {
        return this.parameter;
    }

    public Uri getReturnValue() {
        return this.returnValue;
    }

    public void setReturnValue(Uri returnValue) {
        this.returnValue = returnValue;
    }
}
