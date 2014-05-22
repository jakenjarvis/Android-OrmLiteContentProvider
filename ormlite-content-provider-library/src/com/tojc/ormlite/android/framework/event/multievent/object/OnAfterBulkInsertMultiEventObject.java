package com.tojc.ormlite.android.framework.event.multievent.object;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.tojc.ormlite.android.framework.MatcherPattern;
import com.tojc.ormlite.android.framework.event.multievent.MultiEventObjectBase;

import java.util.List;

/**
 * Created by Jaken on 2014/05/13.
 */
public class OnAfterBulkInsertMultiEventObject extends MultiEventObjectBase {
    private final OrmLiteSqliteOpenHelper helper;
    private final SQLiteDatabase db;
    private final MatcherPattern matcherPattern;
    private final Uri uri;
    private final List<ContentValues> values;

    public OnAfterBulkInsertMultiEventObject(Object source, OrmLiteSqliteOpenHelper helper, SQLiteDatabase db, MatcherPattern matcherPattern, Uri uri, List<ContentValues> values) {
        super(source);
        this.helper = helper;
        this.db = db;
        this.matcherPattern = matcherPattern;
        this.uri = uri;
        this.values = values;
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

    public Uri getUri() {
        return this.uri;
    }

    public List<ContentValues> getValues() {
        return this.values;
    }
}
