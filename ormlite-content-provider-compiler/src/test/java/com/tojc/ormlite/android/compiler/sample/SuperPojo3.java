package com.tojc.ormlite.android.compiler.sample;

import android.provider.BaseColumns;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentMimeTypeVnd;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentUri;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.Contract;

@Contract(contractClassName = "com.tojc.ormlite.android.compiler.sample.SuperPojoContract3")
@DefaultContentUri(authority = "com.tojc.ormlite.android.compiler.samplepojo", path = "my_pojo_3")
@DefaultContentMimeTypeVnd(name = "pojo3", type = "pojo3")
@DatabaseTable(tableName = "pojo3")
public class SuperPojo3 {
    @DatabaseField(columnName = BaseColumns._ID, generatedId = true)
    @AdditionalAnnotation.DefaultSortOrder
    private int id;

    @DatabaseField
    private String name;
}
