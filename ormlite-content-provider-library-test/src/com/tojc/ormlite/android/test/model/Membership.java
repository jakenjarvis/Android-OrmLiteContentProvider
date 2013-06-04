/*
 * This file is part of the Android-OrmLiteContentProvider package.
 *
 * Copyright (c) 2012, Android-OrmLiteContentProvider Team.
 *                     Jaken Jarvis (jaken.jarvis@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * The author may be contacted via
 * https://github.com/jakenjarvis/Android-OrmLiteContentProvider
 */
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
