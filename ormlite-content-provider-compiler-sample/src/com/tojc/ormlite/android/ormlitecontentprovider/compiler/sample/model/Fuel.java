package com.tojc.ormlite.android.ormlitecontentprovider.compiler.sample.model;

import android.provider.BaseColumns;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.Contract;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentMimeTypeVnd;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentUri;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultSortOrder;

@Contract()
@DatabaseTable(tableName = "fuel")
@DefaultContentUri(authority = "com.tojc.ormlite.android.ormlitecontentprovider.compiler.sample", path = "fuel")
@DefaultContentMimeTypeVnd(name = "com.tojc.ormlite.android.ormlitecontentprovider.compiler.sample.provider", type = "fuel")
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
