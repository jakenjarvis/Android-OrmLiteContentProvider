package com.tojc.ormlite.android.test.model;

import android.provider.BaseColumns;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentMimeTypeVnd;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentUri;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultSortOrder;

/**
 * Did you know ? All annotations and parameters are optionnal ! You just need the @Contract
 * @author SNI
 */
@DatabaseTable(tableName = "membership")
@DefaultContentUri(authority = "com.tojc.ormlite.android.test", path = "membership")
@DefaultContentMimeTypeVnd(name = "com.tojc.ormlite.android.test.provider", type = "membership")
public class Membership {

    @DatabaseField(columnName = BaseColumns._ID, generatedId = true)
    @DefaultSortOrder
    private int id;

    @DatabaseField
    private int daysOfMembership;

    public Membership() {
        // ORMLite needs a no-arg constructor
    }

    public Membership(int daysOfMembership) {
        this.id = 0;
        this.daysOfMembership = daysOfMembership;
    }

    public int getId() {
        return id;
    }

    public int getDaysOfMembership() {
        return daysOfMembership;
    }

}
