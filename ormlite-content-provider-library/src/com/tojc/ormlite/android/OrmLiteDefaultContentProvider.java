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
package com.tojc.ormlite.android;

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.tojc.ormlite.android.framework.MatcherController;
import com.tojc.ormlite.android.framework.MatcherPattern;
import com.tojc.ormlite.android.framework.OperationParameters.DeleteParameters;
import com.tojc.ormlite.android.framework.OperationParameters.InsertParameters;
import com.tojc.ormlite.android.framework.OperationParameters.QueryParameters;
import com.tojc.ormlite.android.framework.OperationParameters.UpdateParameters;
import com.tojc.ormlite.android.framework.Parameter;

/**
 * To take advantage of the framework, it provides a standard class. If you use this library, you
 * should inherit from this class (or a provided subclass).
 * @author Jaken
 */
public abstract class OrmLiteDefaultContentProvider<T extends OrmLiteSqliteOpenHelper> extends OrmLiteBaseContentProvider<T> {
    /**
     * Holds an instance of MatcherController. You must be at the stage of initialization, call the
     * add method to class and registration information in table, the pattern required to
     * UriMatcher. In addition, the registration is complete, you must call initialize method in the
     * end.
     */
    private MatcherController controller = null;

    protected void setMatcherController(MatcherController controller) {
        this.controller = controller;
        controller.initialize();
    }

    /**
     * You implement this method. At the timing of query() method, which calls the onQuery().
     * @param helper
     *            This is a helper object. It is the same as one that can be retrieved by
     *            this.getHelper().
     * @param db
     *            This is a SQLiteDatabase object. Return the object obtained by
     *            helper.getReadableDatabase().
     * @param target
     *            It is MatcherPattern objects that match to evaluate Uri by UriMatcher. You can
     *            access information in the tables and columns, ContentUri, MimeType etc.
     * @param parameter
     *            Arguments passed to the query() method.
     * @return Please set value to be returned in the original query() method.
     */
    public abstract Cursor onQuery(T helper, SQLiteDatabase db, MatcherPattern target, QueryParameters parameter);

    /**
     * You implement this method. At the timing of insert() method, which calls the onInsert().
     * @param helper
     *            This is a helper object. It is the same as one that can be retrieved by
     *            this.getHelper().
     * @param db
     *            This is a SQLiteDatabase object. Return the object obtained by
     *            helper.getWritableDatabase().
     * @param target
     *            It is MatcherPattern objects that match to evaluate Uri by UriMatcher. You can
     *            access information in the tables and columns, ContentUri, MimeType etc.
     * @param parameter
     *            Arguments passed to the insert() method.
     * @return Please set value to be returned in the original insert() method.
     */
    public abstract Uri onInsert(T helper, SQLiteDatabase db, MatcherPattern target, InsertParameters parameter);

    /**
     * You implement this method. At the timing of delete() method, which calls the onDelete().
     * @param helper
     *            This is a helper object. It is the same as one that can be retrieved by
     *            this.getHelper().
     * @param db
     *            This is a SQLiteDatabase object. Return the object obtained by
     *            helper.getWritableDatabase().
     * @param target
     *            It is MatcherPattern objects that match to evaluate Uri by UriMatcher. You can
     *            access information in the tables and columns, ContentUri, MimeType etc.
     * @param parameter
     *            Arguments passed to the delete() method.
     * @return Please set value to be returned in the original delete() method.
     */
    public abstract int onDelete(T helper, SQLiteDatabase db, MatcherPattern target, DeleteParameters parameter);

    /**
     * You implement this method. At the timing of update() method, which calls the onUpdate().
     * @param helper
     *            This is a helper object. It is the same as one that can be retrieved by
     *            this.getHelper().
     * @param db
     *            This is a SQLiteDatabase object. Return the object obtained by
     *            helper.getWritableDatabase().
     * @param target
     *            It is MatcherPattern objects that match to evaluate Uri by UriMatcher. You can
     *            access information in the tables and columns, ContentUri, MimeType etc.
     * @param parameter
     *            Arguments passed to the update() method.
     * @return Please set value to be returned in the original update() method.
     */
    public abstract int onUpdate(T helper, SQLiteDatabase db, MatcherPattern target, UpdateParameters parameter);

    /*
     * @see android.content.ContentProvider#getType(android.net.Uri)
     */
    @Override
    public String getType(Uri uri) {
        if (!controller.hasPreinitialized()) {
            throw new IllegalStateException("Controller has not been initialized.");
        }

        int patternCode = controller.getUriMatcher().match(uri);
        MatcherPattern pattern = controller.findMatcherPattern(patternCode);
        if (pattern == null) {
            throw new IllegalArgumentException("unknown uri : " + uri.toString());
        }
        return pattern.getMimeTypeVndString();
    }

    /*
     * @see android.content.ContentProvider#query(android.net.Uri, java.lang.String[],
     * java.lang.String, java.lang.String[], java.lang.String)
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor result = null;

        if (!controller.hasPreinitialized()) {
            throw new IllegalStateException("Controller has not been initialized.");
        }

        int patternCode = controller.getUriMatcher().match(uri);
        MatcherPattern pattern = controller.findMatcherPattern(patternCode);
        if (pattern == null) {
            throw new IllegalArgumentException("unknown uri : " + uri.toString());
        }

        Parameter parameter = new Parameter(uri, projection, selection, selectionArgs, sortOrder);
        SQLiteDatabase db = this.getHelper().getReadableDatabase();

        result = onQuery(this.getHelper(), db, pattern, parameter);
        if (result != null) {
            this.onQueryCompleted(result, uri, pattern, parameter);
        }
        return result;
    }

    /**
     * This method is called after the onQuery processing has been handled. If you're a need,
     * you can override this method.
     * @param result
     *            This is the return value of onQuery method.
     * @param uri
     *            This is the Uri of target.
     * @param target
     *            This is identical to the argument of onQuery method.
     *            It is MatcherPattern objects that match to evaluate Uri by UriMatcher. You can
     *            access information in the tables and columns, ContentUri, MimeType etc.
     * @param parameter
     *            This is identical to the argument of onQuery method.
     *            Arguments passed to the query() method.
     */
    protected void onQueryCompleted(Cursor result, Uri uri, MatcherPattern target, QueryParameters parameter) {
        result.setNotificationUri(this.getContext().getContentResolver(), uri);
    }

    /*
     * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri result = null;

        if (!controller.hasPreinitialized()) {
            throw new IllegalStateException("Controller has not been initialized.");
        }

        int patternCode = controller.getUriMatcher().match(uri);
        MatcherPattern pattern = controller.findMatcherPattern(patternCode);
        if (pattern == null) {
            throw new IllegalArgumentException("unknown uri : " + uri.toString());
        }

        Parameter parameter = new Parameter(uri, values);
        SQLiteDatabase db = this.getHelper().getWritableDatabase();

        result = onInsert(this.getHelper(), db, pattern, parameter);
        if (result != null) {
            this.onInsertCompleted(result, uri, pattern, parameter);
        }
        return result;
    }

    /**
     * This method is called after the onInsert processing has been handled. If you're a need,
     * you can override this method.
     * @param result
     *            This is the return value of onInsert method.
     * @param uri
     *            This is the Uri of target.
     * @param target
     *            This is identical to the argument of onInsert method.
     *            It is MatcherPattern objects that match to evaluate Uri by UriMatcher. You can
     *            access information in the tables and columns, ContentUri, MimeType etc.
     * @param parameter
     *            This is identical to the argument of onInsert method.
     *            Arguments passed to the insert() method.
     */
    protected void onInsertCompleted(Uri result, Uri uri, MatcherPattern target, InsertParameters parameter) {
        this.getContext().getContentResolver().notifyChange(result, null);
    }

    /*
     * @see android.content.ContentProvider#delete(android.net.Uri, java.lang.String,
     * java.lang.String[])
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int result = -1;

        if (!controller.hasPreinitialized()) {
            throw new IllegalStateException("Controller has not been initialized.");
        }

        int patternCode = controller.getUriMatcher().match(uri);
        MatcherPattern pattern = controller.findMatcherPattern(patternCode);
        if (pattern == null) {
            throw new IllegalArgumentException("unknown uri : " + uri.toString());
        }

        Parameter parameter = new Parameter(uri, selection, selectionArgs);
        SQLiteDatabase db = this.getHelper().getWritableDatabase();

        result = onDelete(this.getHelper(), db, pattern, parameter);
        if (result >= 0) {
            this.onDeleteCompleted(result, uri, pattern, parameter);
        }
        return result;
    }

    /**
     * This method is called after the onDelete processing has been handled. If you're a need,
     * you can override this method.
     * @param result
     *            This is the return value of onDelete method.
     * @param uri
     *            This is the Uri of target.
     * @param target
     *            This is identical to the argument of onDelete method.
     *            It is MatcherPattern objects that match to evaluate Uri by UriMatcher. You can
     *            access information in the tables and columns, ContentUri, MimeType etc.
     * @param parameter
     *            This is identical to the argument of onDelete method.
     *            Arguments passed to the delete() method.
     */
    protected void onDeleteCompleted(int result, Uri uri, MatcherPattern target, DeleteParameters parameter) {
        this.getContext().getContentResolver().notifyChange(uri, null);
    }

    /*
     * @see android.content.ContentProvider#update(android.net.Uri, android.content.ContentValues,
     * java.lang.String, java.lang.String[])
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int result = -1;

        if (!controller.hasPreinitialized()) {
            throw new IllegalStateException("Controller has not been initialized.");
        }

        int patternCode = controller.getUriMatcher().match(uri);
        MatcherPattern pattern = controller.findMatcherPattern(patternCode);
        if (pattern == null) {
            throw new IllegalArgumentException("unknown uri : " + uri.toString());
        }

        Parameter parameter = new Parameter(uri, values, selection, selectionArgs);
        SQLiteDatabase db = this.getHelper().getWritableDatabase();

        result = onUpdate(this.getHelper(), db, pattern, parameter);
        if (result >= 0) {
            this.onUpdateCompleted(result, uri, pattern, parameter);
        }
        return result;
    }

    /**
     * This method is called after the onUpdate processing has been handled. If you're a need,
     * you can override this method.
     * @param result
     *            This is the return value of onUpdate method.
     * @param uri
     *            This is the Uri of target.
     * @param target
     *            This is identical to the argument of onUpdate method.
     *            It is MatcherPattern objects that match to evaluate Uri by UriMatcher. You can
     *            access information in the tables and columns, ContentUri, MimeType etc.
     * @param parameter
     *            This is identical to the argument of onUpdate method.
     *            Arguments passed to the update() method.
     */
    protected void onUpdateCompleted(int result, Uri uri, MatcherPattern target, UpdateParameters parameter) {
        this.getContext().getContentResolver().notifyChange(uri, null);
    }

    /*
     * @see android.content.ContentProvider#bulkInsert(android.net.Uri,
     * android.content.ContentValues[])
     */
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        int result = 0;

        if (!controller.hasPreinitialized()) {
            throw new IllegalStateException("Controller has not been initialized.");
        }

        int patternCode = controller.getUriMatcher().match(uri);
        MatcherPattern pattern = controller.findMatcherPattern(patternCode);
        if (pattern == null) {
            throw new IllegalArgumentException("unknown uri : " + uri.toString());
        }

        SQLiteDatabase db = this.getHelper().getWritableDatabase();

        db.beginTransaction();
        try {
            for (ContentValues value : values) {
                Parameter parameter = new Parameter(uri, value);

                Uri resultBulkInsert = this.onBulkInsert(this.getHelper(), db, pattern, parameter);
                if (resultBulkInsert != null) {
                    result++;
                    // this.getContext().getContentResolver().notifyChange(resultBulkInsert, null);
                }
            }
            db.setTransactionSuccessful();

            if (result >= 1) {
                this.onBulkInsertCompleted(result, uri);
            }
        } finally {
            db.endTransaction();
        }
        return result;
    }

    /**
     * You implement this method. At the timing of bulkInsert() method, which calls the
     * onBulkInsert(). Start the transaction, will be called for each record.
     * @param helper
     *            This is a helper object. It is the same as one that can be retrieved by
     *            this.getHelper().
     * @param db
     *            This is a SQLiteDatabase object. Return the object obtained by
     *            helper.getWritableDatabase().
     * @param target
     *            It is MatcherPattern objects that match to evaluate Uri by UriMatcher. You can
     *            access information in the tables and columns, ContentUri, MimeType etc.
     * @param parameter
     *            Arguments passed to the insert() method.
     * @return Please set value to be returned in the original insert() method.
     */
    public Uri onBulkInsert(T helper, SQLiteDatabase db, MatcherPattern target, InsertParameters parameter) {
        return onInsert(helper, db, target, parameter);
    }

    /**
     * This method is called after the bulkInsert processing has been handled. If you're a need,
     * you can override this method.
     * @param result
     *            This is the return value of bulkInsert method.
     * @param uri
     *            This is the Uri of target.
     */
    protected void onBulkInsertCompleted(int result, Uri uri) {
        this.getContext().getContentResolver().notifyChange(uri, null);
    }

    /*
     * @see android.content.ContentProvider#applyBatch(java.util.ArrayList)
     */
    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        ContentProviderResult[] result = null;

        SQLiteDatabase db = this.getHelper().getWritableDatabase();

        db.beginTransaction();
        try {
            result = super.applyBatch(operations);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return result;
    }
}
