package com.tojc.ormlite.android.test.model;

import android.provider.BaseColumns;

import com.j256.ormlite.field.DatabaseField;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultSortOrder;

/**
 * Created by Jaken on 2015/05/06.
 * NOTE: Here we will not be able to define the @DatabaseTable annotation.
 * http://stackoverflow.com/questions/10290207/how-do-i-properly-annotate-inheritance-classes-using-ormlite
 * http://stackoverflow.com/questions/10188414/does-ormlite-support-inheritance
 */
public abstract class AbstractAccount {
    @DatabaseField(columnName = BaseColumns._ID, generatedId = true)
    @DefaultSortOrder
    protected int id;

    @DatabaseField
    protected String name;

    // private member of the superclass.
    @DatabaseField
    private String comment;

    public AbstractAccount() {
        // ORMLite needs a no-arg constructor
    }

    public AbstractAccount(String name, String comment) {
        this.id = 0;
        this.name = name;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }
}
