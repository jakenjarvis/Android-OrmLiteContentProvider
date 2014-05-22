package com.tojc.ormlite.android.test.provider.fragment;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.tojc.ormlite.android.OrmLiteContentProviderFragment;
import com.tojc.ormlite.android.framework.MatcherPattern;
import com.tojc.ormlite.android.framework.OperationParameters;
import com.tojc.ormlite.android.framework.event.EventClasses;
import com.tojc.ormlite.android.event.listenerset.DefaultContentProviderAllListenerSet;
import com.tojc.ormlite.android.test.provider.SampleHelper;
import com.tojc.ormlite.android.test.provider.UnderTestSampleProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jaken on 2014/05/17.
 */
public abstract class UnderTestSampleFragment extends OrmLiteContentProviderFragment<UnderTestSampleProvider, SampleHelper> implements DefaultContentProviderAllListenerSet<SampleHelper> {
    //private Set<EventClasses> eventClasses = EnumSet.noneOf(EventClasses.class);
    private List<EventClasses> eventClasses = new ArrayList<EventClasses>();

    public UnderTestSampleFragment() {
        super();
    }

    public List<EventClasses> getEventClassesList() {
        return this.eventClasses;
    }

    @Override
    public Class<? extends OrmLiteContentProviderFragment<UnderTestSampleProvider, SampleHelper>> getFragmentClass() {
        return UnderTestSampleFragment.class;
    }

    @Override
    public Cursor onQuery(SampleHelper helper, SQLiteDatabase db, MatcherPattern target, OperationParameters.QueryParameters parameter) {
        this.eventClasses.add(EventClasses.OnQuery);
        return this.getContentProvider().onQuery(helper, db, target, parameter);
    }

    @Override
    public void onQueryCompleted(Cursor result, Uri uri, MatcherPattern target, OperationParameters.QueryParameters parameter) {
        this.eventClasses.add(EventClasses.OnQueryCompleted);
        this.getContentProvider().onQueryCompleted(result, uri, target, parameter);
    }

    @Override
    public Uri onInsert(SampleHelper helper, SQLiteDatabase db, MatcherPattern target, OperationParameters.InsertParameters parameter) {
        this.eventClasses.add(EventClasses.OnInsert);
        return this.getContentProvider().onInsert(helper, db, target, parameter);
    }

    @Override
    public void onInsertCompleted(Uri result, Uri uri, MatcherPattern target, OperationParameters.InsertParameters parameter) {
        this.eventClasses.add(EventClasses.OnInsertCompleted);
        this.getContentProvider().onInsertCompleted(result, uri, target, parameter);
    }

    @Override
    public int onDelete(SampleHelper helper, SQLiteDatabase db, MatcherPattern target, OperationParameters.DeleteParameters parameter) {
        this.eventClasses.add(EventClasses.OnDelete);
        return this.getContentProvider().onDelete(helper, db, target, parameter);
    }

    @Override
    public void onDeleteCompleted(int result, Uri uri, MatcherPattern target, OperationParameters.DeleteParameters parameter) {
        this.eventClasses.add(EventClasses.OnDeleteCompleted);
        this.getContentProvider().onDeleteCompleted(result, uri, target, parameter);
    }

    @Override
    public int onUpdate(SampleHelper helper, SQLiteDatabase db, MatcherPattern target, OperationParameters.UpdateParameters parameter) {
        this.eventClasses.add(EventClasses.OnUpdate);
        return this.getContentProvider().onUpdate(helper, db, target, parameter);
    }

    @Override
    public void onUpdateCompleted(int result, Uri uri, MatcherPattern target, OperationParameters.UpdateParameters parameter) {
        this.eventClasses.add(EventClasses.OnUpdateCompleted);
        this.getContentProvider().onUpdateCompleted(result, uri, target, parameter);
    }

    @Override
    public void onBeforeBulkInsert(SampleHelper helper, SQLiteDatabase db, MatcherPattern target, Uri uri, List<ContentValues> values) {
        this.eventClasses.add(EventClasses.OnBeforeBulkInsert);
        this.getContentProvider().onBeforeBulkInsert(helper, db, target, uri, values);
    }

    @Override
    public Uri onBulkInsert(SampleHelper helper, SQLiteDatabase db, MatcherPattern target, OperationParameters.InsertParameters parameter) {
        this.eventClasses.add(EventClasses.OnBulkInsert);
        return this.getContentProvider().onBulkInsert(helper, db, target, parameter);
    }

    @Override
    public void onAfterBulkInsert(SampleHelper helper, SQLiteDatabase db, MatcherPattern target, Uri uri, List<ContentValues> values) {
        this.eventClasses.add(EventClasses.OnAfterBulkInsert);
        this.getContentProvider().onAfterBulkInsert(helper, db, target, uri, values);
    }

    @Override
    public void onBulkInsertCompleted(int result, Uri uri) {
        this.eventClasses.add(EventClasses.OnBulkInsertCompleted);
        this.getContentProvider().onBulkInsertCompleted(result, uri);
    }

    @Override
    public void onBeforeApplyBatch(SampleHelper helper, SQLiteDatabase db, ArrayList<ContentProviderOperation> operations) {
        this.eventClasses.add(EventClasses.OnBeforeApplyBatch);
        this.getContentProvider().onBeforeApplyBatch(helper, db, operations);
    }

    @Override
    public void onAfterApplyBatch(SampleHelper helper, SQLiteDatabase db, ArrayList<ContentProviderOperation> operations, ContentProviderResult[] result) {
        this.eventClasses.add(EventClasses.OnAfterApplyBatch);
        this.getContentProvider().onAfterApplyBatch(helper, db, operations, result);
    }
}
