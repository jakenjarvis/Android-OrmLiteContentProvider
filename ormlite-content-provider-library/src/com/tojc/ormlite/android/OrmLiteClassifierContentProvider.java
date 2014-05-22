package com.tojc.ormlite.android;

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
import com.tojc.ormlite.android.framework.Parameter;
import com.tojc.ormlite.android.framework.event.EventClasses;
import com.tojc.ormlite.android.framework.event.EventController;
import com.tojc.ormlite.android.framework.event.FragmentEventHandling;
import com.tojc.ormlite.android.event.listenerset.DefaultContentProviderAllListenerSet;
import com.tojc.ormlite.android.framework.event.multievent.object.OnAfterApplyBatchMultiEventObject;
import com.tojc.ormlite.android.framework.event.multievent.object.OnAfterBulkInsertMultiEventObject;
import com.tojc.ormlite.android.framework.event.multievent.object.OnBeforeApplyBatchMultiEventObject;
import com.tojc.ormlite.android.framework.event.multievent.object.OnBeforeBulkInsertMultiEventObject;
import com.tojc.ormlite.android.framework.event.multievent.object.OnBulkInsertCompletedMultiEventObject;
import com.tojc.ormlite.android.framework.event.multievent.object.OnBulkInsertMultiEventObject;
import com.tojc.ormlite.android.framework.event.multievent.object.OnDeleteCompletedMultiEventObject;
import com.tojc.ormlite.android.framework.event.multievent.object.OnDeleteMultiEventObject;
import com.tojc.ormlite.android.framework.event.multievent.object.OnInsertCompletedMultiEventObject;
import com.tojc.ormlite.android.framework.event.multievent.object.OnInsertMultiEventObject;
import com.tojc.ormlite.android.framework.event.multievent.object.OnQueryCompletedMultiEventObject;
import com.tojc.ormlite.android.framework.event.multievent.object.OnQueryMultiEventObject;
import com.tojc.ormlite.android.framework.event.multievent.object.OnUpdateCompletedMultiEventObject;
import com.tojc.ormlite.android.framework.event.multievent.object.OnUpdateMultiEventObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.List;
import java.util.Map;

/**
 * This class is a base class to control the event-handling. Express the standard structure,
 * which classifies the program by event-handling.
 *
 * @author Jaken
 * @since 1.0.5
 */
public abstract class OrmLiteClassifierContentProvider<T extends OrmLiteSqliteOpenHelper> extends OrmLiteBaseContentProvider<T> implements DefaultContentProviderAllListenerSet<T> {
    private static final String EVENT_DEFAULT_KEY = "__default__";

    /**
     * This is the object which manages the event-handling.
     * It has registered an event-handling of ContentProvider here.
     */
    private EventController eventController = new EventController();

    /**
     * To register an object that notify an event. Without overwriting, notified will be added.
     * It is necessary to implement the following interfaces in order to notify.
     *
     * @param key      Specify a unique event listener key to identify.(Grouping for each object)
     * @param listener Listener object to register
     * @see com.tojc.ormlite.android.event.listener
     * @see com.tojc.ormlite.android.event.listenerset
     */
    public void registerEventListenerObject(String key, Object listener) {
        this.eventController.registerEventListenerObject(key, listener);
    }

    /**
     * Holds an instance of MatcherController. You must be at the stage of initialization, call the
     * add method to class and registration information in table, the pattern required to
     * UriMatcher. In addition, the registration is complete, you must call initialize method in the
     * end.
     */
    private MatcherController matcherController = null;

    protected void setMatcherController(MatcherController controller) {
        this.matcherController = controller;
        controller.initialize();

        this.onRegisterEventListenerObject(this.matcherController);
    }

    /**
     * Called at the time to register event listeners.
     * If you want to change the registration process, please override this method.
     * You do not need to be changed in normal conditions of use.
     */
    protected void onRegisterEventListenerObject(MatcherController controller) {
        // Register an event listener for ContentProvider.
        this.registerEventListenerObject(EVENT_DEFAULT_KEY, this);

        // Register an event listener for all of ContentProviderFragments.
        for (Map.Entry<String, OrmLiteContentProviderFragment<?, ?>> entry : controller.getContentProviderFragments().entrySet()) {
            this.registerEventListenerObject(entry.getKey(), entry.getValue());
        }
    }

    /**
     * ContentProviderFragment that has been added to MatcherController.
     * This is for reference only. Please do not operate the collection.
     *
     * @return contentProviderFragments
     * @since 1.0.5
     * @see com.tojc.ormlite.android.framework.MatcherController#getContentProviderFragments()
     */
    public Map<String, OrmLiteContentProviderFragment<?, ?>> getContentProviderFragments() {
        return this.matcherController.getContentProviderFragments();
    }

    /*
     * @see android.content.ContentProvider#getType(android.net.Uri)
     */
    @Override
    public String getType(Uri uri) {
        if (!this.matcherController.hasPreinitialized()) {
            throw new IllegalStateException("Controller has not been initialized.");
        }

        int patternCode = this.matcherController.getUriMatcher().match(uri);
        MatcherPattern pattern = this.matcherController.findMatcherPattern(patternCode);
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
        if (!this.matcherController.hasPreinitialized()) {
            throw new IllegalStateException("Controller has not been initialized.");
        }

        int patternCode = this.matcherController.getUriMatcher().match(uri);
        MatcherPattern pattern = this.matcherController.findMatcherPattern(patternCode);
        if (pattern == null) {
            throw new IllegalArgumentException("unknown uri : " + uri.toString());
        }

        Parameter parameter = new Parameter(uri, projection, selection, selectionArgs, sortOrder);
        SQLiteDatabase db = this.getHelper().getReadableDatabase();

        return this.internalOnQuery(null, db, pattern, parameter);
    }

    /**
     * @see android.content.ContentProvider#query(android.net.Uri, java.lang.String[],
     * java.lang.String, java.lang.String[], java.lang.String)
     * @see com.tojc.ormlite.android.event.listener.OnQueryListener
     * @see com.tojc.ormlite.android.event.listener.OnQueryCompletedListener
     * @since 1.0.5
     */
    protected Cursor internalOnQuery(Cursor result, SQLiteDatabase db, MatcherPattern pattern, Parameter parameter) {
        Uri uri = parameter.getUri();

        OnQueryMultiEventObject paramOnQuery = new OnQueryMultiEventObject(this, this.getHelper(), db, pattern, parameter);
        paramOnQuery.setReturnValue(result);
        this.raiseEvent(EventClasses.OnQuery, paramOnQuery, pattern);
        result = paramOnQuery.getReturnValue();

        if (result != null) {
            OnQueryCompletedMultiEventObject paramOnQueryCompleted = new OnQueryCompletedMultiEventObject(this, result, uri, pattern, parameter);
            this.raiseEvent(EventClasses.OnQueryCompleted, paramOnQueryCompleted, pattern);
        }
        return result;
    }

    /*
     * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (!this.matcherController.hasPreinitialized()) {
            throw new IllegalStateException("Controller has not been initialized.");
        }

        int patternCode = this.matcherController.getUriMatcher().match(uri);
        MatcherPattern pattern = this.matcherController.findMatcherPattern(patternCode);
        if (pattern == null) {
            throw new IllegalArgumentException("unknown uri : " + uri.toString());
        }

        Parameter parameter = new Parameter(uri, values);
        SQLiteDatabase db = this.getHelper().getWritableDatabase();

        return this.internalOnInsert(null, db, pattern, parameter);
    }

    /**
     * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
     * @see com.tojc.ormlite.android.event.listener.OnInsertListener
     * @see com.tojc.ormlite.android.event.listener.OnInsertCompletedListener
     * @since 1.0.5
     */
    protected Uri internalOnInsert(Uri result, SQLiteDatabase db, MatcherPattern pattern, Parameter parameter) {
        Uri uri = parameter.getUri();

        OnInsertMultiEventObject paramOnInsert = new OnInsertMultiEventObject(this, this.getHelper(), db, pattern, parameter);
        paramOnInsert.setReturnValue(result);
        this.raiseEvent(EventClasses.OnInsert, paramOnInsert, pattern);
        result = paramOnInsert.getReturnValue();

        if (result != null) {
            OnInsertCompletedMultiEventObject paramOnInsertCompleted = new OnInsertCompletedMultiEventObject(this, result, uri, pattern, parameter);
            this.raiseEvent(EventClasses.OnInsertCompleted, paramOnInsertCompleted, pattern);
        }
        return result;
    }

    /*
     * @see android.content.ContentProvider#delete(android.net.Uri, java.lang.String,
     * java.lang.String[])
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (!this.matcherController.hasPreinitialized()) {
            throw new IllegalStateException("Controller has not been initialized.");
        }

        int patternCode = this.matcherController.getUriMatcher().match(uri);
        MatcherPattern pattern = this.matcherController.findMatcherPattern(patternCode);
        if (pattern == null) {
            throw new IllegalArgumentException("unknown uri : " + uri.toString());
        }

        Parameter parameter = new Parameter(uri, selection, selectionArgs);
        SQLiteDatabase db = this.getHelper().getWritableDatabase();

        return this.internalOnDelete(-1, db, pattern, parameter);
    }

    /**
     * @see android.content.ContentProvider#delete(android.net.Uri, java.lang.String,
     * java.lang.String[])
     * @see com.tojc.ormlite.android.event.listener.OnDeleteListener
     * @see com.tojc.ormlite.android.event.listener.OnDeleteCompletedListener
     * @since 1.0.5
     */
    protected int internalOnDelete(int result, SQLiteDatabase db, MatcherPattern pattern, Parameter parameter) {
        Uri uri = parameter.getUri();

        OnDeleteMultiEventObject paramOnDelete = new OnDeleteMultiEventObject(this, this.getHelper(), db, pattern, parameter);
        paramOnDelete.setReturnValue(result);
        this.raiseEvent(EventClasses.OnDelete, paramOnDelete, pattern);
        result = paramOnDelete.getReturnValue();

        if (result >= 0) {
            OnDeleteCompletedMultiEventObject paramOnDeleteCompleted = new OnDeleteCompletedMultiEventObject(this, result, uri, pattern, parameter);
            this.raiseEvent(EventClasses.OnDeleteCompleted, paramOnDeleteCompleted, pattern);
        }
        return result;
    }

    /*
     * @see android.content.ContentProvider#update(android.net.Uri, android.content.ContentValues,
     * java.lang.String, java.lang.String[])
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (!this.matcherController.hasPreinitialized()) {
            throw new IllegalStateException("Controller has not been initialized.");
        }

        int patternCode = this.matcherController.getUriMatcher().match(uri);
        MatcherPattern pattern = this.matcherController.findMatcherPattern(patternCode);
        if (pattern == null) {
            throw new IllegalArgumentException("unknown uri : " + uri.toString());
        }

        Parameter parameter = new Parameter(uri, values, selection, selectionArgs);
        SQLiteDatabase db = this.getHelper().getWritableDatabase();

        return this.internalOnUpdate(-1, db, pattern, parameter);
    }

    /**
     * @see android.content.ContentProvider#update(android.net.Uri, android.content.ContentValues,
     * java.lang.String, java.lang.String[])
     * @see com.tojc.ormlite.android.event.listener.OnUpdateListener
     * @see com.tojc.ormlite.android.event.listener.OnUpdateCompletedListener
     * @since 1.0.5
     */
    protected int internalOnUpdate(int result, SQLiteDatabase db, MatcherPattern pattern, Parameter parameter) {
        Uri uri = parameter.getUri();

        OnUpdateMultiEventObject paramOnUpdate = new OnUpdateMultiEventObject(this, this.getHelper(), db, pattern, parameter);
        paramOnUpdate.setReturnValue(result);
        this.raiseEvent(EventClasses.OnUpdate, paramOnUpdate, pattern);
        result = paramOnUpdate.getReturnValue();

        if (result >= 0) {
            OnUpdateCompletedMultiEventObject paramOnUpdateCompleted = new OnUpdateCompletedMultiEventObject(this, result, uri, pattern, parameter);
            this.raiseEvent(EventClasses.OnUpdateCompleted, paramOnUpdateCompleted, pattern);
        }
        return result;
    }

    /*
     * @see android.content.ContentProvider#bulkInsert(android.net.Uri,
     * android.content.ContentValues[])
     * @since 1.0.1
     */
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        if (!this.matcherController.hasPreinitialized()) {
            throw new IllegalStateException("Controller has not been initialized.");
        }

        int patternCode = this.matcherController.getUriMatcher().match(uri);
        MatcherPattern pattern = this.matcherController.findMatcherPattern(patternCode);
        if (pattern == null) {
            throw new IllegalArgumentException("unknown uri : " + uri.toString());
        }

        SQLiteDatabase db = this.getHelper().getWritableDatabase();

        return this.internalOnBulkInsert(0, db, pattern, uri, values);
    }

    /**
     * @see android.content.ContentProvider#bulkInsert(android.net.Uri,
     * android.content.ContentValues[])
     * @see com.tojc.ormlite.android.event.listener.OnBulkInsertListener
     * @see com.tojc.ormlite.android.event.listener.OnBulkInsertCompletedListener
     * @since 1.0.5
     */
    protected int internalOnBulkInsert(int result, SQLiteDatabase db, MatcherPattern pattern, Uri uri, ContentValues[] values) {
        db.beginTransaction();
        try {
            List<ContentValues> arrayBeforeBulkInsertValues = new ArrayList<ContentValues>(Arrays.asList(values));
            OnBeforeBulkInsertMultiEventObject paramOnBeforeBulkInsert = new OnBeforeBulkInsertMultiEventObject(this, this.getHelper(), db, pattern, uri, arrayBeforeBulkInsertValues);
            this.raiseEvent(EventClasses.OnBeforeBulkInsert, paramOnBeforeBulkInsert, pattern);

            for (ContentValues value : values) {
                Parameter parameter = new Parameter(uri, value);

                OnBulkInsertMultiEventObject paramOnBulkInsert = new OnBulkInsertMultiEventObject(this, this.getHelper(), db, pattern, parameter);
                paramOnBulkInsert.setReturnValue(null);
                this.raiseEvent(EventClasses.OnBulkInsert, paramOnBulkInsert, pattern);
                Uri resultBulkInsert = paramOnBulkInsert.getReturnValue();

                if (resultBulkInsert != null) {
                    result++;
                }
            }

            List<ContentValues> arrayAfterBulkInsertValues = new ArrayList<ContentValues>(Arrays.asList(values));
            OnAfterBulkInsertMultiEventObject paramOnAfterBulkInsert = new OnAfterBulkInsertMultiEventObject(this, this.getHelper(), db, pattern, uri, arrayAfterBulkInsertValues);
            this.raiseEvent(EventClasses.OnAfterBulkInsert, paramOnAfterBulkInsert, pattern);

            db.setTransactionSuccessful();

            if (result >= 1) {
                OnBulkInsertCompletedMultiEventObject paramOnBulkInsertCompleted = new OnBulkInsertCompletedMultiEventObject(this, result, uri);
                this.raiseEvent(EventClasses.OnBulkInsertCompleted, paramOnBulkInsertCompleted, pattern);
            }
        } finally {
            db.endTransaction();
        }
        return result;
    }

    /*
     * @see android.content.ContentProvider#applyBatch(java.util.ArrayList)
     * @since 1.0.1
     */
    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        SQLiteDatabase db = this.getHelper().getWritableDatabase();

        return this.internalOnApplyBatch(null, db, operations);
    }

    /**
     * @see android.content.ContentProvider#applyBatch(java.util.ArrayList)
     * @see com.tojc.ormlite.android.event.listener.OnBeforeApplyBatchListener
     * @see com.tojc.ormlite.android.event.listener.OnAfterApplyBatchListener
     * @since 1.0.5
     */
    protected ContentProviderResult[] internalOnApplyBatch(ContentProviderResult[] result, SQLiteDatabase db, ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        db.beginTransaction();
        try {
            // MEMO: Notify all listeners.
            OnBeforeApplyBatchMultiEventObject paramOnBeforeApplyBatch = new OnBeforeApplyBatchMultiEventObject(this, this.getHelper(), db, operations);
            this.raiseEvent(EventClasses.OnBeforeApplyBatch, paramOnBeforeApplyBatch, null);

            result = super.applyBatch(operations);

            // MEMO: Notify all listeners.
            OnAfterApplyBatchMultiEventObject paramOnAfterApplyBatch = new OnAfterApplyBatchMultiEventObject(this, this.getHelper(), db, operations, result);
            this.raiseEvent(EventClasses.OnAfterApplyBatch, paramOnAfterApplyBatch, null);

            if (result != null) {
                db.setTransactionSuccessful();
            }
        } finally {
            db.endTransaction();
        }
        return result;
    }

    private <V extends EventObject> void raiseEvent(EventClasses eventClasses, V param, MatcherPattern pattern) {
        if (pattern != null) {
            OrmLiteContentProviderFragment<?, ?> fragment = pattern.getParentContentProviderFragment();
            if (fragment != null) {
                this.onFragmentEventHandling(eventClasses, param, fragment);
            } else {
                this.raise(eventClasses, EVENT_DEFAULT_KEY, param);
            }
        } else {
            // Calling all listeners.
            this.raise(eventClasses, null, param);
        }
    }

    /**
     * @param eventClasses
     * @param param
     * @param fragment
     * @param <V>
     * @see com.tojc.ormlite.android.framework.event.FragmentEventHandling
     * @see com.tojc.ormlite.android.OrmLiteContentProviderFragment#getFragmentEventHandling()
     */
    protected <V extends EventObject> void onFragmentEventHandling(EventClasses eventClasses, V param, OrmLiteContentProviderFragment<?, ?> fragment) {
        String key = fragment.getKeyName();
        int handling = fragment.getFragmentEventHandling();
        switch (handling) {
            case FragmentEventHandling.FRAGMENT_ONLY:
                this.raise(eventClasses, key, param);
                break;

            case FragmentEventHandling.FRAGMENT_AND_DEFAULT_FORWARD:
                if (this.containsEventKey(eventClasses, key)) {
                    this.raise(eventClasses, key, param);
                } else {
                    this.raise(eventClasses, EVENT_DEFAULT_KEY, param);
                }
                break;

            case FragmentEventHandling.FRAGMENT_AND_DEFAULT_DUPLICATE:
                this.raise(eventClasses, EVENT_DEFAULT_KEY, param);
                this.raise(eventClasses, key, param); // Prefer the return value of the fragment.
                break;

            default:
                break;
        }
    }

    protected final <V extends EventObject> void raise(EventClasses eventClasses, String key, V param) {
        this.eventController.raise(eventClasses, key, param);
    }

    protected final boolean containsEventKey(EventClasses eventClasses, String key) {
        return this.eventController.containsEventKey(eventClasses, key);
    }
}
