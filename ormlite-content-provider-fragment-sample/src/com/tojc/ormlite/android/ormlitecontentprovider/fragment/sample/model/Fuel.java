package com.tojc.ormlite.android.ormlitecontentprovider.fragment.sample.model;

import android.provider.BaseColumns;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentMimeTypeVnd;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentUri;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultSortOrder;
import com.tojc.ormlite.android.ormlitecontentprovider.fragment.sample.provider.FuelContract;

/**
 * Created by Jaken on 2014/05/10.
 */
@DatabaseTable(tableName = FuelContract.TABLE_NAME)
@DefaultContentUri(authority = FuelContract.AUTHORITY, path = FuelContract.CONTENT_URI_PATH)
@DefaultContentMimeTypeVnd(name = FuelContract.MIMETYPE_NAME, type = FuelContract.MIMETYPE_TYPE)
public class Fuel {
    @DatabaseField(columnName = BaseColumns._ID, generatedId = true)
    @DefaultSortOrder
    private int id;

    @DatabaseField(canBeNull = false, unique = true)
    private String name;

    public Fuel() {
        // ORMLite needs a no-arg constructor
    }

    public Fuel(String name) {
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}
