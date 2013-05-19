package com.tojc.ormlite.android.test.provider;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.tojc.ormlite.android.test.model.Account;
import com.tojc.ormlite.android.test.model.Membership;

public class SampleHelper extends OrmLiteSqliteOpenHelper {

    /* package-private */static final Class<?>[] CLASS_LIST = new Class<?>[] {Account.class, Membership.class};

    public SampleHelper(Context context) {
        super(context, "MyDatabase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            resetAllTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            resetAllTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resetAllTables() throws SQLException {
        for (Class<?> clazz : CLASS_LIST) {
            TableUtils.dropTable(connectionSource, clazz, true);
            TableUtils.createTable(connectionSource, clazz);
        }
    }
}
