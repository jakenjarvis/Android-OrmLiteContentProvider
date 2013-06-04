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
package com.tojc.ormlite.android.framework;

import android.content.ContentValues;
import android.net.Uri;

import com.tojc.ormlite.android.framework.OperationParameters.DeleteParameters;
import com.tojc.ormlite.android.framework.OperationParameters.InsertParameters;
import com.tojc.ormlite.android.framework.OperationParameters.OperationParametersBaseInterface;
import com.tojc.ormlite.android.framework.OperationParameters.QueryParameters;
import com.tojc.ormlite.android.framework.OperationParameters.UpdateParameters;

/**
 * Implementation class that holds the parameter.
 * @author Jaken
 */
public class Parameter implements OperationParametersBaseInterface, QueryParameters, InsertParameters,
    DeleteParameters, UpdateParameters {
    // Android Event ITEM
    private Uri uri;
    private String[] projection;
    private String selection;
    private String[] selectionArgs;
    private String sortOrder;
    private ContentValues values;

    public Parameter() {
        this.clear();
    }

    public void clear() {
        this.uri = null;
        this.projection = null;
        this.selection = null;
        this.selectionArgs = null;
        this.sortOrder = null;
        this.values = null;
    }

    // query
    public Parameter(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        this.clear();
        this.uri = uri;
        if (projection != null) {
            this.projection = projection.clone();
        }
        this.selection = selection;
        if (selectionArgs != null) {
            this.selectionArgs = selectionArgs.clone();
        }
        this.sortOrder = sortOrder;
    }

    // insert
    public Parameter(Uri uri, ContentValues values) {
        this.clear();
        this.uri = uri;
        this.values = values;
    }

    // delete
    public Parameter(Uri uri, String selection, String[] selectionArgs) {
        this.clear();
        this.uri = uri;
        this.selection = selection;
        if (selectionArgs != null) {
            this.selectionArgs = selectionArgs.clone();
        }
    }

    // update
    public Parameter(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        this.clear();
        this.uri = uri;
        this.values = values;
        this.selection = selection;
        if (selectionArgs != null) {
            this.selectionArgs = selectionArgs.clone();
        }
    }

    @Override
    public Uri getUri() {
        return this.uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    @Override
    public String[] getProjection() {
        if (projection == null) {
            return null;
        }
        return this.projection.clone();
    }

    public void setProjection(String[] projection) {
        if (projection == null) {
            this.projection = null;
        } else {
            this.projection = projection.clone();
        }
    }

    @Override
    public String getSelection() {
        return this.selection;
    }

    public void setSelection(String selection) {
        this.selection = selection;
    }

    @Override
    public String[] getSelectionArgs() {
        if (this.selectionArgs != null) {
            return this.selectionArgs.clone();
        } else {
            return null;
        }
    }

    public void setSelectionArgs(String[] selectionArgs) {
        if (selectionArgs == null) {
            this.selectionArgs = null;
        } else {
            this.selectionArgs = selectionArgs.clone();
        }
    }

    @Override
    public String getSortOrder() {
        return this.sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public ContentValues getValues() {
        return this.values;
    }

    public void setValues(ContentValues values) {
        this.values = values;
    }
}
