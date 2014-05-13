package com.tojc.ormlite.android.ormlitecontentprovider.fragment.sample.model;

import android.provider.BaseColumns;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.Contract;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentMimeTypeVnd;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentUri;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultSortOrder;
import com.tojc.ormlite.android.ormlitecontentprovider.fragment.sample.provider.CarContract;

/**
 * Created by Jaken on 2014/05/10.
 */

@DatabaseTable(tableName = CarContract.TABLE_NAME)
@DefaultContentUri(authority = CarContract.AUTHORITY, path = CarContract.CONTENT_URI_PATH)
@DefaultContentMimeTypeVnd(name = CarContract.MIMETYPE_NAME, type = CarContract.MIMETYPE_TYPE)
public class Car {
    @DatabaseField(columnName = BaseColumns._ID, generatedId = true)
    @DefaultSortOrder
    private int id;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Fuel fuel;

    public Car() {
        // ORMLite needs a no-arg constructor
    }

    public Car(String name, Fuel fuel) {
        this.name = name;
        this.fuel = fuel;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Fuel getFuel() {
        return this.fuel;
    }
}
