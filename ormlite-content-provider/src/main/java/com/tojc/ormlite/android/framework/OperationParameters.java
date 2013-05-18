/*
 * This file is part of the Android-OrmLiteContentProvider package.
 * 
 * Copyright (c) 2012, Jaken Jarvis (jaken.jarvis@gmail.com)
 * 
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 * 
 * The author may be contacted via 
 * https://github.com/jakenjarvis/Android-OrmLiteContentProvider
 */
package com.tojc.ormlite.android.framework;

import android.content.ContentValues;
import android.net.Uri;

/**
 * This keeps the parameters of Operation. Through the interface and exposes only the methods
 * required for event.
 * @author Jaken
 */
public class OperationParameters {
    public interface OperationParametersBaseInterface {
        public Uri getUri();
    }

    public interface QueryParameters extends OperationParametersBaseInterface {
        public String[] getProjection();

        public String getSelection();

        public String[] getSelectionArgs();

        public String getSortOrder();
    }

    public interface InsertParameters extends OperationParametersBaseInterface {
        public ContentValues getValues();
    }

    public interface DeleteParameters extends OperationParametersBaseInterface {
        public String getSelection();

        public String[] getSelectionArgs();
    }

    public interface UpdateParameters extends OperationParametersBaseInterface {
        public ContentValues getValues();

        public String getSelection();

        public String[] getSelectionArgs();
    }

    /**
     * Implementation class that holds the parameter.
     * @author Jaken
     */
    public static class Parameter implements OperationParametersBaseInterface, QueryParameters, InsertParameters, DeleteParameters, UpdateParameters {
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
            this.projection = projection;
            this.selection = selection;
            this.selectionArgs = selectionArgs;
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
            this.selectionArgs = selectionArgs;
        }

        // update
        public Parameter(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
            this.clear();
            this.uri = uri;
            this.values = values;
            this.selection = selection;
            this.selectionArgs = selectionArgs;
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
            return this.projection;
        }

        public void setProjection(String[] projection) {
            this.projection = projection;
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
            return this.selectionArgs;
        }

        public void setSelectionArgs(String[] selectionArgs) {
            this.selectionArgs = selectionArgs;
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
}
