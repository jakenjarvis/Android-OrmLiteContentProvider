package com.tojc.ormlite.android.compiler.sample;

import android.provider.BaseColumns;
import com.j256.ormlite.field.DatabaseField;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.Contract;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultSortOrder;

@Contract(contractClassName = "com.tojc.ormlite.android.compiler.sample.CombinedPojoContract1")
public class CombinedPojo2 {
    @DatabaseField(columnName = BaseColumns._ID, generatedId = true)
    @DefaultSortOrder
    private int id;

    @DatabaseField
    private String name;
}
