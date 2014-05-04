package com.tojc.ormlite.android.test.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentMimeTypeVnd;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentUri;

/**
 * Created by Jaken on 2014/05/04.
 */
@DatabaseTable(tableName = "inheritance_accounts")
@DefaultContentUri(authority = "com.tojc.ormlite.android.test", path = "inheritance_accounts")
@DefaultContentMimeTypeVnd(name = "com.tojc.ormlite.android.test.provider", type = "inheritance_accounts")
public class InheritanceAccount extends BaseAccount
{
    public InheritanceAccount() {
        // ORMLite needs a no-arg constructor
        super();
    }

    private InheritanceAccount(String name) {
        super(name);
    }

    public InheritanceAccount(String name, String address) {
        super(name);
        this.address = address;
    }

    @DatabaseField
    private String address;

    public String getAddress() {
        return address;
    }
}
