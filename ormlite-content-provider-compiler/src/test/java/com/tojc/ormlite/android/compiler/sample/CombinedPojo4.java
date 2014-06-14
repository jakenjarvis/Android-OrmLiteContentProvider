package com.tojc.ormlite.android.compiler.sample;

import android.provider.BaseColumns;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentMimeTypeVnd;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentUri;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.Contract;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultSortOrder;

@Contract(contractClassName = "com.tojc.ormlite.android.compiler.sample.CombinedPojoContract2")
@DefaultContentUri(authority = "com.tojc.ormlite.android.compiler.samplepojo", path = "my_pojo_4")
@DefaultContentMimeTypeVnd(name = "pojo4", type = "pojo4")
@DatabaseTable(tableName = "pojo4")
public class CombinedPojo4 {
    @DatabaseField(columnName = BaseColumns._ID, generatedId = true)
    @DefaultSortOrder
    private int id;

    @DatabaseField
    private String name;
}
