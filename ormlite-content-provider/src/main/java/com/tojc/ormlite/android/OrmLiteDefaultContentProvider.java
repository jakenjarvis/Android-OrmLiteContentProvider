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
package com.tojc.ormlite.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.tojc.ormlite.android.annotation.info.ContentMimeTypeVndInfo;
import com.tojc.ormlite.android.annotation.info.ContentUriInfo;
import com.tojc.ormlite.android.framework.MatcherPattern;
import com.tojc.ormlite.android.framework.MimeTypeVnd.SubType;
import com.tojc.ormlite.android.framework.OperationParameters.DeleteParameters;
import com.tojc.ormlite.android.framework.OperationParameters.InsertParameters;
import com.tojc.ormlite.android.framework.OperationParameters.Parameter;
import com.tojc.ormlite.android.framework.OperationParameters.QueryParameters;
import com.tojc.ormlite.android.framework.OperationParameters.UpdateParameters;
import com.tojc.ormlite.android.framework.TableInfo;

/**
 * To take advantage of the framework, it provides a standard class. If you use this library, you
 * should inherit from this class.
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
     * Before ContentProvider instance is created, you need to register a pattern to UriMatcher.
     * MatcherController will help the registration process.
     * @author Jaken
     */
    protected static class MatcherController {
        private boolean preinitialized = false;

        private UriMatcher matcher = null;
        private Map<Class<?>, TableInfo> tables = null;
        private List<MatcherPattern> matcherPatterns = null;

        private TableInfo lastAddTableInfo = null;

        public MatcherController() {
            this.matcher = new UriMatcher(UriMatcher.NO_MATCH);
            this.tables = new HashMap<Class<?>, TableInfo>();
            this.matcherPatterns = new ArrayList<MatcherPattern>();

            this.lastAddTableInfo = null;
        }

        /**
         * Register a class for table.
         * @param tableClassType
         *            Register a class for table.
         * @return Instance of the MatcherController class.
         */
        public MatcherController add(Class<?> tableClassType) {
            this.addTableClass(tableClassType);
            return this;
        }

        /**
         * Register a class for table. And registers a pattern for UriMatcher.
         * @param tableClassType
         *            Register a class for table.
         * @param subType
         *            Contents to be registered in the pattern, specify single or multiple. This is
         *            used in the MIME types. * Item : If the URI pattern is for a single row :
         *            vnd.android.cursor.item/ * Directory : If the URI pattern is for more than one
         *            row : vnd.android.cursor.dir/
         * @param pattern
         *            registers a pattern for UriMatcher. Note: Must not contain the name of path
         *            here. ex) content://com.example.app.provider/table1 : pattern = ""
         *            content://com.example.app.provider/table1/# : pattern = "#"
         *            content://com.example.app.provider/table1/dataset2 : pattern = "dataset2"
         * @param patternCode
         *            UriMatcher code is returned
         * @return Instance of the MatcherController class.
         */
        public MatcherController add(Class<?> tableClassType, SubType subType, String pattern, int patternCode) {
            this.addTableClass(tableClassType);
            this.addMatcherPattern(subType, pattern, patternCode);
            return this;
        }

        /**
         * Registers a pattern for UriMatcher. It refer to the class that was last registered from
         * add method.
         * @param subType
         *            Contents to be registered in the pattern, specify single or multiple. This is
         *            used in the MIME types. * Item : If the URI pattern is for a single row :
         *            vnd.android.cursor.item/ * Directory : If the URI pattern is for more than one
         *            row : vnd.android.cursor.dir/
         * @param pattern
         *            registers a pattern for UriMatcher. Note: Must not contain the name of path
         *            here. ex) content://com.example.app.provider/table1 : pattern = ""
         *            content://com.example.app.provider/table1/# : pattern = "#"
         *            content://com.example.app.provider/table1/dataset2 : pattern = "dataset2"
         * @param patternCode
         *            UriMatcher code is returned
         * @return Instance of the MatcherController class.
         */
        public MatcherController add(SubType subType, String pattern, int patternCode) {
            this.addMatcherPattern(subType, pattern, patternCode);
            return this;
        }

        /**
         * Registers a pattern for UriMatcher. To register you have to create an instance of
         * MatcherPattern.
         * @param matcherPattern
         *            register MatcherPattern.
         * @return Instance of the MatcherController class.
         */
        public MatcherController add(MatcherPattern matcherPattern) {
            int patternCode = matcherPattern.getPatternCode();

            if (this.lastAddTableInfo == null) {
                throw new IllegalStateException("There is a problem with the order of function call.");
            }

            if (findMatcherPattern(patternCode) != null) {
                throw new IllegalArgumentException("patternCode has been specified already exists.");
            }

            this.matcherPatterns.add(matcherPattern);
            return this;
        }

        /**
         * Set the DefaultContentUri. If you did not use the DefaultContentUri annotation, you must
         * call this method.
         * @see com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentUri
         * @param authority
         * @param path
         * @return Instance of the MatcherController class.
         */
        public MatcherController setDefaultContentUri(String authority, String path) {
            if (this.lastAddTableInfo == null) {
                throw new IllegalStateException("There is a problem with the order of function call.");
            }
            this.lastAddTableInfo.setDefaultContentUriInfo(new ContentUriInfo(authority, path));
            return this;
        }

        /**
         * Set the DefaultContentMimeTypeVnd. If you did not use the DefaultContentMimeTypeVnd
         * annotation, you must call this method.
         * @see com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentMimeTypeVnd
         * @param name
         * @param type
         * @return Instance of the MatcherController class.
         */
        public MatcherController setDefaultContentMimeTypeVnd(String name, String type) {
            if (this.lastAddTableInfo == null) {
                throw new IllegalStateException("There is a problem with the order of function call.");
            }
            this.lastAddTableInfo.setDefaultContentMimeTypeVndInfo(new ContentMimeTypeVndInfo(name, type));
            return this;
        }

        /**
         * initialized with the contents that are registered by the add method. This method checks
         * the registration details.
         * @return Instance of the MatcherController class.
         */
        public MatcherController initialize() {
            this.lastAddTableInfo = null;

            for (Map.Entry<Class<?>, TableInfo> entry : this.tables.entrySet()) {
                entry.getValue().isValid(true);
            }

            for (MatcherPattern entry : matcherPatterns) {
                entry.isValid(true);
                this.matcher.addURI(entry.getTableInfo().getDefaultContentUriInfo().getAuthority(), entry.getPathAndPatternString(), entry.getPatternCode());
                entry.setPreinitialized();
            }

            this.preinitialized = true;
            return this;
        }

        /**
         * This will search the MatcherPattern that are registered based on the return code
         * UriMatcher.
         * @param patternCode
         *            UriMatcher code is returned
         * @return Instance of the MatcherPattern class. if no match is found will return null.
         */
        public MatcherPattern findMatcherPattern(int patternCode) {
            MatcherPattern result = null;
            for (MatcherPattern entry : this.matcherPatterns) {
                if (entry.getPatternCode() == patternCode) {
                    result = entry;
                    break;
                }
            }
            return result;
        }

        private TableInfo addTableClass(Class<?> tableClassType) {
            TableInfo result = null;
            if (this.tables.containsKey(tableClassType)) {
                result = this.tables.get(tableClassType);
            } else {
                result = new TableInfo(tableClassType);
                this.tables.put(tableClassType, result);
            }

            // referenced in addMatcherPattern
            this.lastAddTableInfo = result;
            return result;
        }

        private MatcherPattern addMatcherPattern(SubType subType, String pattern, int patternCode) {
            MatcherPattern result = null;

            if (this.lastAddTableInfo == null) {
                throw new IllegalStateException("There is a problem with the order of function call.");
            }

            if (findMatcherPattern(patternCode) != null) {
                throw new IllegalArgumentException("patternCode has been specified already exists.");
            }

            result = new MatcherPattern(this.lastAddTableInfo, subType, pattern, patternCode);
            this.matcherPatterns.add(result);
            return result;
        }

        public boolean hasPreinitialized() {
            return this.preinitialized;
        }

        /**
         * @return Return an instance of the UriMatcher.
         */
        public UriMatcher getUriMatcher() {
            if (!this.preinitialized) {
                throw new IllegalStateException("Controller has not been initialized.");
            }
            return this.matcher;
        }

        /**
         * @return Return a map of tables that have been registered class.
         */
        public Map<Class<?>, TableInfo> getTables() {
            if (!this.preinitialized) {
                throw new IllegalStateException("Controller has not been initialized.");
            }
            return this.tables;
        }

        /**
         * @return Return an instance of the UriMatcher.
         */
        public List<MatcherPattern> getMatcherPatterns() {
            if (!this.preinitialized) {
                throw new IllegalStateException("Controller has not been initialized.");
            }
            return this.matcherPatterns;
        }
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
            result.setNotificationUri(this.getContext().getContentResolver(), uri);
        }
        return result;
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
            this.getContext().getContentResolver().notifyChange(result, null);
        }
        return result;
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
            this.getContext().getContentResolver().notifyChange(uri, null);
        }
        return result;
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
            this.getContext().getContentResolver().notifyChange(uri, null);
        }
        return result;
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

            this.getContext().getContentResolver().notifyChange(uri, null);
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
