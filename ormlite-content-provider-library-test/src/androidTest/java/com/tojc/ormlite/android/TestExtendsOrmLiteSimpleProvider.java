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

import android.app.Instrumentation;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.provider.BaseColumns;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.mock.MockContentResolver;
import android.test.suitebuilder.annotation.MediumTest;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.tojc.ormlite.android.test.model.ExtendsAccount;
import com.tojc.ormlite.android.test.provider.ExtendsAccountContract;
import com.tojc.ormlite.android.test.provider.SampleHelper;
import com.tojc.ormlite.android.test.provider.UnderTestSampleProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class TestExtendsOrmLiteSimpleProvider {

    private static final String TEST_NAME_1 = "Yamada Tarou";
    private static final String TEST_NAME_2 = "Stephane Nicolas";
    private static final String TEST_COMMENT_1 = "Somewhere living in Japan";
    private static final String TEST_COMMENT_2 = "Somewhere living in Internet";
    private static final String TEST_ADDRESS_1 = "Japan";
    private static final String TEST_ADDRESS_2 = "Internet";

    private Instrumentation instrumentation;
    private MockContentResolver resolver;

    public void injectInstrumentation(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
        assertNotNull(this.instrumentation);
        assertNotNull(this.instrumentation.getContext());
        assertNotNull(this.instrumentation.getTargetContext());
    }

    public Instrumentation getInstrumentation() {
        return this.instrumentation;
    }

    @Before
    public void setUp() throws Exception {
        //super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        getHelper().resetAllTables();

        UnderTestSampleProvider provider = new UnderTestSampleProvider();
        provider.attachInfo(getInstrumentation().getTargetContext(), null);

        this.resolver = new MockContentResolver();
        this.resolver.addProvider(ExtendsAccountContract.AUTHORITY, provider);
    }

    @Test
    public void testDoORMLiteCanBeAccessToThePrivateMembersOfSuperclass() {
        // https://github.com/jakenjarvis/Android-OrmLiteContentProvider/issues/15
        ExtendsAccount account = new ExtendsAccount(TEST_NAME_2, TEST_COMMENT_2, TEST_ADDRESS_2);
        RuntimeExceptionDao<ExtendsAccount, Integer> simpleDao = getHelper().getRuntimeExceptionDao(ExtendsAccount.class);
        simpleDao.create(account);

        List<ExtendsAccount> accountList = simpleDao.queryForAll();
        assertEquals(1, accountList.size());
        assertEquals(TEST_NAME_2, accountList.get(0).getName());
        assertEquals(TEST_COMMENT_2, accountList.get(0).getComment()); // private member
        assertEquals(TEST_ADDRESS_2, accountList.get(0).getAddress());
    }

    @Test
    public void testOnInsert() {
        // given
        ContentValues values = new ContentValues();
        values.clear();
        values.put(ExtendsAccountContract.NAME, TEST_NAME_1);
        values.put(ExtendsAccountContract.COMMENT, TEST_COMMENT_1);
        values.put(ExtendsAccountContract.ADDRESS, TEST_ADDRESS_1);

        // when
        this.resolver.insert(ExtendsAccountContract.CONTENT_URI, values);

        // then
        RuntimeExceptionDao<ExtendsAccount, Integer> simpleDao = getHelper().getRuntimeExceptionDao(ExtendsAccount.class);
        List<ExtendsAccount> accountList = simpleDao.queryForAll();
        assertEquals(1, accountList.size());
        assertEquals(TEST_NAME_1, accountList.get(0).getName());
        assertEquals(TEST_COMMENT_1, accountList.get(0).getComment());
        assertEquals(TEST_ADDRESS_1, accountList.get(0).getAddress());
    }

    @Test
    public void testOnDelete() {
        // given
        ExtendsAccount account = new ExtendsAccount(TEST_NAME_2, TEST_COMMENT_2, TEST_ADDRESS_2);
        RuntimeExceptionDao<ExtendsAccount, Integer> simpleDao = getHelper().getRuntimeExceptionDao(ExtendsAccount.class);
        simpleDao.create(account);
        List<ExtendsAccount> accountList = simpleDao.queryForAll();
        assertEquals(1, accountList.size());

        // when
        this.resolver.delete(ExtendsAccountContract.CONTENT_URI, BaseColumns._ID + " = " + account.getId(), null);

        // then
        accountList = simpleDao.queryForAll();
        assertEquals(0, accountList.size());
    }

    @Test
    public void testOnUpdate() {
        // given
        ExtendsAccount account = new ExtendsAccount(TEST_NAME_1, TEST_COMMENT_1, TEST_ADDRESS_1);
        RuntimeExceptionDao<ExtendsAccount, Integer> simpleDao = getHelper().getRuntimeExceptionDao(ExtendsAccount.class);
        simpleDao.create(account);
        List<ExtendsAccount> accountList = simpleDao.queryForAll();
        assertEquals(1, accountList.size());

        ContentValues values = new ContentValues();
        values.clear();
        values.put(ExtendsAccountContract.NAME, TEST_NAME_2);

        // when
        this.resolver.update(ExtendsAccountContract.CONTENT_URI, values, BaseColumns._ID + " = " + account.getId(), null);

        // then
        accountList = simpleDao.queryForAll();
        assertEquals(1, accountList.size());
        assertEquals(TEST_NAME_2, accountList.get(0).getName());
        assertEquals(TEST_COMMENT_1, accountList.get(0).getComment());
        assertEquals(TEST_ADDRESS_1, accountList.get(0).getAddress());
    }

    @Test
    public void testOnQuery() {
        // given
        ExtendsAccount account1 = new ExtendsAccount(TEST_NAME_1, TEST_COMMENT_1, TEST_ADDRESS_1);
        ExtendsAccount account2 = new ExtendsAccount(TEST_NAME_2, TEST_COMMENT_2, TEST_ADDRESS_2);
        RuntimeExceptionDao<ExtendsAccount, Integer> simpleDao = getHelper().getRuntimeExceptionDao(ExtendsAccount.class);
        simpleDao.create(account1);
        simpleDao.create(account2);
        List<ExtendsAccount> accountList = simpleDao.queryForAll();
        assertEquals(2, accountList.size());

        // when
        Cursor cursor = this.resolver.query(ExtendsAccountContract.CONTENT_URI, new String[]{BaseColumns._ID, ExtendsAccountContract.NAME, ExtendsAccountContract.COMMENT, ExtendsAccountContract.ADDRESS}, null, null, null);
        accountList = new ArrayList<ExtendsAccount>();
        while (cursor.moveToNext()) {
            ExtendsAccount account = new ExtendsAccount(
                    cursor.getString(cursor.getColumnIndex(ExtendsAccountContract.NAME)),
                    cursor.getString(cursor.getColumnIndex(ExtendsAccountContract.COMMENT)),
                    cursor.getString(cursor.getColumnIndex(ExtendsAccountContract.ADDRESS))
                    );
            accountList.add(account);
        }
        cursor.close();

        // then
        assertEquals(2, accountList.size());
        assertEquals(TEST_NAME_1, accountList.get(0).getName());
        assertEquals(TEST_COMMENT_1, accountList.get(0).getComment());
        assertEquals(TEST_ADDRESS_1, accountList.get(0).getAddress());
        assertEquals(TEST_NAME_2, accountList.get(1).getName());
        assertEquals(TEST_COMMENT_2, accountList.get(1).getComment());
        assertEquals(TEST_ADDRESS_2, accountList.get(1).getAddress());
    }

    @Test
    public void testOnQueryWithOrder() {
        // given
        ExtendsAccount account1 = new ExtendsAccount(TEST_NAME_1, TEST_COMMENT_1, TEST_ADDRESS_1);
        ExtendsAccount account2 = new ExtendsAccount(TEST_NAME_2, TEST_COMMENT_2, TEST_ADDRESS_2);
        RuntimeExceptionDao<ExtendsAccount, Integer> simpleDao = getHelper().getRuntimeExceptionDao(ExtendsAccount.class);
        simpleDao.create(account1);
        simpleDao.create(account2);
        List<ExtendsAccount> accountList = simpleDao.queryForAll();
        assertEquals(2, accountList.size());

        // when
        String order = BaseColumns._ID + " DESC";
        Cursor cursor = this.resolver.query(ExtendsAccountContract.CONTENT_URI, new String[]{BaseColumns._ID, ExtendsAccountContract.NAME, ExtendsAccountContract.COMMENT, ExtendsAccountContract.ADDRESS}, null, null, order);
        accountList = new ArrayList<ExtendsAccount>();
        while (cursor.moveToNext()) {
            ExtendsAccount account = new ExtendsAccount(
                    cursor.getString(cursor.getColumnIndex(ExtendsAccountContract.NAME)),
                    cursor.getString(cursor.getColumnIndex(ExtendsAccountContract.COMMENT)),
                    cursor.getString(cursor.getColumnIndex(ExtendsAccountContract.ADDRESS))
                    );
            accountList.add(account);
        }
        cursor.close();

        // then
        assertEquals(2, accountList.size());
        assertEquals(TEST_NAME_2, accountList.get(0).getName());
        assertEquals(TEST_COMMENT_2, accountList.get(0).getComment());
        assertEquals(TEST_ADDRESS_2, accountList.get(0).getAddress());
        assertEquals(TEST_NAME_1, accountList.get(1).getName());
        assertEquals(TEST_COMMENT_1, accountList.get(1).getComment());
        assertEquals(TEST_ADDRESS_1, accountList.get(1).getAddress());
    }

    @Test
    public void testContentProviderAcquisition() throws RemoteException {
        // given
        ExtendsAccount account1 = new ExtendsAccount(TEST_NAME_1, TEST_COMMENT_1, TEST_ADDRESS_1);
        ExtendsAccount account2 = new ExtendsAccount(TEST_NAME_2, TEST_COMMENT_2, TEST_ADDRESS_2);
        RuntimeExceptionDao<ExtendsAccount, Integer> simpleDao = getHelper().getRuntimeExceptionDao(ExtendsAccount.class);
        simpleDao.create(account1);
        simpleDao.create(account2);
        List<ExtendsAccount> accountList = simpleDao.queryForAll();
        assertEquals(2, accountList.size());

        // when
        ContentProviderClient contentProviderClient = this.resolver.acquireContentProviderClient(ExtendsAccountContract.CONTENT_URI);
        Cursor cursor = contentProviderClient.query(ExtendsAccountContract.CONTENT_URI, null, null, null, null);

        // then
        accountList = new ArrayList<ExtendsAccount>();
        while (cursor.moveToNext()) {
            ExtendsAccount account = new ExtendsAccount(
                    cursor.getString(cursor.getColumnIndex(ExtendsAccountContract.NAME)),
                    cursor.getString(cursor.getColumnIndex(ExtendsAccountContract.COMMENT)),
                    cursor.getString(cursor.getColumnIndex(ExtendsAccountContract.ADDRESS))
                    );
            accountList.add(account);
        }
        cursor.close();
        contentProviderClient.release();

        assertEquals(2, accountList.size());
        assertEquals(TEST_NAME_1, accountList.get(0).getName());
        assertEquals(TEST_COMMENT_1, accountList.get(0).getComment());
        assertEquals(TEST_ADDRESS_1, accountList.get(0).getAddress());
        assertEquals(TEST_NAME_2, accountList.get(1).getName());
        assertEquals(TEST_COMMENT_2, accountList.get(1).getComment());
        assertEquals(TEST_ADDRESS_2, accountList.get(1).getAddress());
    }

    @Test
    public void testBulkInsert() {
        // given
        final int testExtendsAccountCount = 10;
        ContentValues[] contentValues = new ContentValues[testExtendsAccountCount];
        for (int accountIndex = 0; accountIndex < testExtendsAccountCount; accountIndex++) {
            ContentValues values = new ContentValues();
            values.clear();
            values.put(ExtendsAccountContract.NAME, TEST_NAME_1 + accountIndex);
            values.put(ExtendsAccountContract.COMMENT, TEST_COMMENT_1 + accountIndex);
            values.put(ExtendsAccountContract.ADDRESS, TEST_ADDRESS_1 + accountIndex);
            contentValues[accountIndex] = values;
        }
        // when
        this.resolver.bulkInsert(ExtendsAccountContract.CONTENT_URI, contentValues);

        // then
        RuntimeExceptionDao<ExtendsAccount, Integer> simpleDao = getHelper().getRuntimeExceptionDao(ExtendsAccount.class);
        List<ExtendsAccount> accountList = simpleDao.queryForAll();
        assertEquals(testExtendsAccountCount, accountList.size());
        int accountIndex = 0;
        for (ExtendsAccount account : accountList) {
            assertEquals(TEST_NAME_1 + accountIndex, account.getName());
            assertEquals(TEST_COMMENT_1 + accountIndex, account.getComment());
            assertEquals(TEST_ADDRESS_1 + accountIndex, account.getAddress());
            accountIndex++;
        }
    }

    @Test
    public void testApplyBatch() throws RemoteException, OperationApplicationException {
        // given
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
        operations.add(ContentProviderOperation.newInsert(ExtendsAccountContract.CONTENT_URI).withValue(ExtendsAccountContract.NAME, TEST_NAME_1).withValue(ExtendsAccountContract.COMMENT, TEST_COMMENT_1).withValue(ExtendsAccountContract.ADDRESS, TEST_ADDRESS_1).build());
        operations.add(ContentProviderOperation.newInsert(ExtendsAccountContract.CONTENT_URI).withValue(ExtendsAccountContract.NAME, TEST_NAME_2).withValue(ExtendsAccountContract.COMMENT, TEST_COMMENT_2).withValue(ExtendsAccountContract.ADDRESS, TEST_ADDRESS_2).build());

        // when
        this.resolver.applyBatch(ExtendsAccountContract.AUTHORITY, operations);

        // then
        RuntimeExceptionDao<ExtendsAccount, Integer> simpleDao = getHelper().getRuntimeExceptionDao(ExtendsAccount.class);
        List<ExtendsAccount> accountList = simpleDao.queryForAll();
        assertEquals(2, accountList.size());
        assertEquals(TEST_NAME_1, accountList.get(0).getName());
        assertEquals(TEST_COMMENT_1, accountList.get(0).getComment());
        assertEquals(TEST_ADDRESS_1, accountList.get(0).getAddress());
        assertEquals(TEST_NAME_2, accountList.get(1).getName());
        assertEquals(TEST_COMMENT_2, accountList.get(1).getComment());
        assertEquals(TEST_ADDRESS_2, accountList.get(1).getAddress());
    }

    private SampleHelper getHelper() {
        return new SampleHelper(getInstrumentation().getTargetContext());
    }
}
