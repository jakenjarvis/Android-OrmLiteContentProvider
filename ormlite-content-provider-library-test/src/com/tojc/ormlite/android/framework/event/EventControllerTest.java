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
package com.tojc.ormlite.android.framework.event;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.test.AndroidTestCase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.tojc.ormlite.android.framework.MatcherPattern;
import com.tojc.ormlite.android.framework.OperationParameters;
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
import java.util.List;

/**
 * Created by Jaken on 2014/05/08.
 */
public class EventControllerTest extends AndroidTestCase {

    private static final String TEST_AUTHORITY = "tojc.com";

    private final Uri targetTestUri = new Uri.Builder()
            .scheme("http")
            .authority(TEST_AUTHORITY)
            .build();

    public void testEventController_the_call_all_events() {
        EventController eventController = new EventController();

        TestClassA testListener = new TestClassA();
        eventController.registerEventListenerObject(null, testListener);

        OnQueryMultiEventObject paramOnQuery = new OnQueryMultiEventObject(this, null, null, null, null);
        eventController.raise(EventClasses.OnQuery, paramOnQuery);
        assertEquals(testListener.getCount(), 1);
        assertEquals(paramOnQuery.getReturnValue().getCount(), 1000);

        OnQueryCompletedMultiEventObject paramOnQueryCompleted = new OnQueryCompletedMultiEventObject(this, null, null, null, null);
        eventController.raise(EventClasses.OnQueryCompleted, paramOnQueryCompleted);
        assertEquals(testListener.getCount(), 3);

        OnInsertMultiEventObject paramOnInsert = new OnInsertMultiEventObject(this, null, null, null, null);
        eventController.raise(EventClasses.OnInsert, paramOnInsert);
        assertEquals(testListener.getCount(), 7);
        assertEquals(paramOnInsert.getReturnValue().toString(), targetTestUri.toString());

        OnInsertCompletedMultiEventObject paramOnInsertCompleted = new OnInsertCompletedMultiEventObject(this, null, null, null, null);
        eventController.raise(EventClasses.OnInsertCompleted, paramOnInsertCompleted);
        assertEquals(testListener.getCount(), 15);

        OnDeleteMultiEventObject paramOnDelete = new OnDeleteMultiEventObject(this, null, null, null, null);
        eventController.raise(EventClasses.OnDelete, paramOnDelete);
        assertEquals(testListener.getCount(), 31);
        assertEquals(paramOnDelete.getReturnValue(), 1000);

        OnDeleteCompletedMultiEventObject paramOnDeleteCompleted = new OnDeleteCompletedMultiEventObject(this, 0, null, null, null);
        eventController.raise(EventClasses.OnDeleteCompleted, paramOnDeleteCompleted);
        assertEquals(testListener.getCount(), 63);

        OnUpdateMultiEventObject paramOnUpdate = new OnUpdateMultiEventObject(this, null, null, null, null);
        eventController.raise(EventClasses.OnUpdate, paramOnUpdate);
        assertEquals(testListener.getCount(), 127);
        assertEquals(paramOnUpdate.getReturnValue(), 1000);

        OnUpdateCompletedMultiEventObject paramOnUpdateCompleted = new OnUpdateCompletedMultiEventObject(this, 0, null, null, null);
        eventController.raise(EventClasses.OnUpdateCompleted, paramOnUpdateCompleted);
        assertEquals(testListener.getCount(), 255);

        OnBeforeBulkInsertMultiEventObject paramOnBeforeBulkInsert = new OnBeforeBulkInsertMultiEventObject(this, null, null, null, null, null);
        eventController.raise(EventClasses.OnBeforeBulkInsert, paramOnBeforeBulkInsert);
        assertEquals(testListener.getCount(), 511);

        OnBulkInsertMultiEventObject paramOnBulkInsert = new OnBulkInsertMultiEventObject(this, null, null, null, null);
        eventController.raise(EventClasses.OnBulkInsert, paramOnBulkInsert);
        assertEquals(testListener.getCount(), 1023);
        assertEquals(paramOnBulkInsert.getReturnValue().toString(), targetTestUri.toString());

        OnAfterBulkInsertMultiEventObject paramOnAfterBulkInsert = new OnAfterBulkInsertMultiEventObject(this, null, null, null, null, null);
        eventController.raise(EventClasses.OnAfterBulkInsert, paramOnAfterBulkInsert);
        assertEquals(testListener.getCount(), 2047);

        OnBulkInsertCompletedMultiEventObject paramOnBulkInsertCompleted = new OnBulkInsertCompletedMultiEventObject(this, 0, null);
        eventController.raise(EventClasses.OnBulkInsertCompleted, paramOnBulkInsertCompleted);
        assertEquals(testListener.getCount(), 4095);

        OnBeforeApplyBatchMultiEventObject paramOnBeforeApplyBatch = new OnBeforeApplyBatchMultiEventObject(this, null, null, null);
        eventController.raise(EventClasses.OnBeforeApplyBatch, paramOnBeforeApplyBatch);
        assertEquals(testListener.getCount(), 8191);

        OnAfterApplyBatchMultiEventObject paramOnAfterApplyBatch = new OnAfterApplyBatchMultiEventObject(this, null, null, null, null);
        eventController.raise(EventClasses.OnAfterApplyBatch, paramOnAfterApplyBatch);
        assertEquals(testListener.getCount(), 16383);
    }


    // ----------------------------------
    // CLASSES UNDER TEST
    // ----------------------------------
    private class TestClassA implements DefaultContentProviderAllListenerSet<OrmLiteSqliteOpenHelper> {
        private int count = 0;

        public TestClassA() {
            this.count = 0;
        }

        public int getCount() {
            return this.count;
        }

        @Override
        public Cursor onQuery(OrmLiteSqliteOpenHelper helper, SQLiteDatabase db, MatcherPattern target, OperationParameters.QueryParameters parameter) {
            this.count += 1;
            return new MockCursor(1000);
        }

        @Override
        public void onQueryCompleted(Cursor result, Uri uri, MatcherPattern target, OperationParameters.QueryParameters parameter) {
            this.count += 2;
        }

        @Override
        public Uri onInsert(OrmLiteSqliteOpenHelper helper, SQLiteDatabase db, MatcherPattern target, OperationParameters.InsertParameters parameter) {
            this.count += 4;
            return targetTestUri;
        }

        @Override
        public void onInsertCompleted(Uri result, Uri uri, MatcherPattern target, OperationParameters.InsertParameters parameter) {
            this.count += 8;
        }

        @Override
        public int onDelete(OrmLiteSqliteOpenHelper helper, SQLiteDatabase db, MatcherPattern target, OperationParameters.DeleteParameters parameter) {
            this.count += 16;
            return 1000;
        }

        @Override
        public void onDeleteCompleted(int result, Uri uri, MatcherPattern target, OperationParameters.DeleteParameters parameter) {
            this.count += 32;
        }

        @Override
        public int onUpdate(OrmLiteSqliteOpenHelper helper, SQLiteDatabase db, MatcherPattern target, OperationParameters.UpdateParameters parameter) {
            this.count += 64;
            return 1000;
        }

        @Override
        public void onUpdateCompleted(int result, Uri uri, MatcherPattern target, OperationParameters.UpdateParameters parameter) {
            this.count += 128;
        }

        @Override
        public void onBeforeBulkInsert(OrmLiteSqliteOpenHelper helper, SQLiteDatabase db, MatcherPattern target, Uri uri, List<ContentValues> values) {
            this.count += 256;
        }

        @Override
        public Uri onBulkInsert(OrmLiteSqliteOpenHelper helper, SQLiteDatabase db, MatcherPattern target, OperationParameters.InsertParameters parameter) {
            this.count += 512;
            return targetTestUri;
        }

        @Override
        public void onAfterBulkInsert(OrmLiteSqliteOpenHelper helper, SQLiteDatabase db, MatcherPattern target, Uri uri, List<ContentValues> values) {
            this.count += 1024;
        }

        @Override
        public void onBulkInsertCompleted(int result, Uri uri) {
            this.count += 2048;
        }

        @Override
        public void onBeforeApplyBatch(OrmLiteSqliteOpenHelper helper, SQLiteDatabase db, ArrayList<ContentProviderOperation> operations) {
            this.count += 4096;
        }

        @Override
        public void onAfterApplyBatch(OrmLiteSqliteOpenHelper helper, SQLiteDatabase db, ArrayList<ContentProviderOperation> operations, ContentProviderResult[] result) {
            this.count += 8192;
        }
    }


    private class MockCursor implements Cursor {
        private int count = 0;
        public MockCursor(int count) {
            this.count = count;
        }

        @Override
        public int getCount() {
            return this.count;
        }

        @Override
        public int getPosition() {
            return 0;
        }

        @Override
        public boolean move(int offset) {
            return false;
        }

        @Override
        public boolean moveToPosition(int position) {
            return false;
        }

        @Override
        public boolean moveToFirst() {
            return false;
        }

        @Override
        public boolean moveToLast() {
            return false;
        }

        @Override
        public boolean moveToNext() {
            return false;
        }

        @Override
        public boolean moveToPrevious() {
            return false;
        }

        @Override
        public boolean isFirst() {
            return false;
        }

        @Override
        public boolean isLast() {
            return false;
        }

        @Override
        public boolean isBeforeFirst() {
            return false;
        }

        @Override
        public boolean isAfterLast() {
            return false;
        }

        @Override
        public int getColumnIndex(String columnName) {
            return 0;
        }

        @Override
        public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
            return 0;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return null;
        }

        @Override
        public String[] getColumnNames() {
            return new String[0];
        }

        @Override
        public int getColumnCount() {
            return 0;
        }

        @Override
        public byte[] getBlob(int columnIndex) {
            return new byte[0];
        }

        @Override
        public String getString(int columnIndex) {
            return null;
        }

        @Override
        public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {

        }

        @Override
        public short getShort(int columnIndex) {
            return 0;
        }

        @Override
        public int getInt(int columnIndex) {
            return 0;
        }

        @Override
        public long getLong(int columnIndex) {
            return 0;
        }

        @Override
        public float getFloat(int columnIndex) {
            return 0;
        }

        @Override
        public double getDouble(int columnIndex) {
            return 0;
        }

        @Override
        public int getType(int columnIndex) {
            return 0;
        }

        @Override
        public boolean isNull(int columnIndex) {
            return false;
        }

        @Override
        public void deactivate() {

        }

        @Override
        public boolean requery() {
            return false;
        }

        @Override
        public void close() {

        }

        @Override
        public boolean isClosed() {
            return false;
        }

        @Override
        public void registerContentObserver(ContentObserver observer) {

        }

        @Override
        public void unregisterContentObserver(ContentObserver observer) {

        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void setNotificationUri(ContentResolver cr, Uri uri) {

        }

        @Override
        public Uri getNotificationUri() {
            return null;
        }

        @Override
        public boolean getWantsAllOnMoveCalls() {
            return false;
        }

        @Override
        public Bundle getExtras() {
            return null;
        }

        @Override
        public Bundle respond(Bundle extras) {
            return null;
        }
    }



}
