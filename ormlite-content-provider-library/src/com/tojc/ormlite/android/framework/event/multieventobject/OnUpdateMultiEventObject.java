package com.tojc.ormlite.android.framework.event.multieventobject;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.tojc.ormlite.android.framework.MatcherPattern;
import com.tojc.ormlite.android.framework.OperationParameters;
import com.tojc.ormlite.android.framework.Parameter;
import com.tojc.ormlite.android.framework.event.multieventbase.MultiEventObjectBase;

/**
 * Created by Jaken on 2014/05/06.
 */
public class OnUpdateMultiEventObject extends MultiEventObjectBase {
    private final OrmLiteSqliteOpenHelper helper;
    private final SQLiteDatabase db;
    private final MatcherPattern matcherPattern;
    private final Parameter parameter;

    private int returnValue;

    public OnUpdateMultiEventObject(Object source, OrmLiteSqliteOpenHelper helper, SQLiteDatabase db, MatcherPattern matcherPattern, Parameter parameter) {
        super(source);
        this.helper = helper;
        this.db = db;
        this.matcherPattern = matcherPattern;
        this.parameter = parameter;

        this.returnValue = 0;
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

    public OperationParameters.UpdateParameters getParameter() {
        return this.parameter;
    }

    public int getReturnValue() {
        return this.returnValue;
    }

    public void setReturnValue(int returnValue) {
        this.returnValue = returnValue;
    }
}
