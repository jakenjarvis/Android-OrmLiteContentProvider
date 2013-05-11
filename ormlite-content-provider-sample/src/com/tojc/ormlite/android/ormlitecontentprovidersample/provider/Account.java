package com.tojc.ormlite.android.ormlitecontentprovidersample.provider;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentMimeTypeVnd;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentUri;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultSortOrder;

@DatabaseTable(tableName = Contract.Account.TABLENAME)
@DefaultContentUri(authority=Contract.AUTHORITY, path=Contract.Account.CONTENT_URI_PATH)
@DefaultContentMimeTypeVnd(name=Contract.Account.MIMETYPE_NAME, type=Contract.Account.MIMETYPE_TYPE)
public class Account
{
    @DatabaseField(columnName = Contract.Account._ID, generatedId = true)
    @DefaultSortOrder
    private int id;

    @DatabaseField
    private String name;

    public Account()
    {
        // ORMLite needs a no-arg constructor
    }

    public Account(String name)
    {
        this.id = 0;
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }
}
