package com.tojc.ormlite.android.ormlitecontentprovidersample.provider;

import android.provider.BaseColumns;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.Contract;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentMimeTypeVnd;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentUri;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultSortOrder;

/**
 * Did you know ? All annotations and parameters are optionnal ! You just need the @Contract
 * @author SNI
 */
@Contract()
@DatabaseTable(tableName = "accounts")
@DefaultContentUri(authority = "com.tojc.ormlite.android.ormlitecontentprovidersample.provider", path = "accounts")
@DefaultContentMimeTypeVnd(name = "com.tojc.ormlite.android.ormlitecontentprovidersample", type = "accounts")
public class Account {

    @DatabaseField(columnName = BaseColumns._ID, generatedId = true)
    @DefaultSortOrder
    private int id;

    @DatabaseField
    private String name;

    public Account() {
        // ORMLite needs a no-arg constructor
    }

    public Account(String name) {
        this.id = 0;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
