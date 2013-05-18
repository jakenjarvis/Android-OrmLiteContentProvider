package com.tojc.ormlite.android.ormlitecontentprovider.sample.provider;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.tojc.ormlite.android.ormlitecontentprovider.sample.model.Account;

public class SampleHelper extends OrmLiteSqliteOpenHelper {
    public SampleHelper(Context context) {
        super(context, "MyDatabase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Account.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Account.class, true);
            TableUtils.createTable(connectionSource, Account.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
