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
import java.util.List;

import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.provider.BaseColumns;
import android.test.InstrumentationTestCase;
import android.test.mock.MockContentResolver;
import android.test.suitebuilder.annotation.MediumTest;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.tojc.ormlite.android.test.model.InheritanceAccount;
import com.tojc.ormlite.android.test.provider.InheritanceAccountContract;
import com.tojc.ormlite.android.test.provider.SampleHelper;
import com.tojc.ormlite.android.test.provider.UnderTestSampleProvider;

@MediumTest
public class TestOrmLiteSimpleProviderInheritanceTest extends InstrumentationTestCase {

    private static final String TEST_NAME_1 = "Yamada Tarou";
    private static final String TEST_NAME_2 = "Stephane Nicolas";
    private static final String TEST_ADDRESS_1 = "Otofuke-cho,Katou-gun,Hokkaido,Japan";
    private static final String TEST_ADDRESS_2 = "Tokyo,Tokyo-to,Tokyo Metropolice,Japan";

    private MockContentResolver resolver;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getHelper().resetAllTables();

        UnderTestSampleProvider provider = new UnderTestSampleProvider();
        provider.attachInfo(getInstrumentation().getContext(), null);

        this.resolver = new MockContentResolver();
        this.resolver.addProvider(InheritanceAccountContract.AUTHORITY, provider);
    }

    public void testOnInsert() {
        // given
        ContentValues values = new ContentValues();
        values.clear();
        values.put(InheritanceAccountContract.NAME, TEST_NAME_1);
        values.put(InheritanceAccountContract.ADDRESS, TEST_ADDRESS_1);

        // when
        this.resolver.insert(InheritanceAccountContract.CONTENT_URI, values);

        // then
        RuntimeExceptionDao<InheritanceAccount, Integer> simpleDao = getHelper().getRuntimeExceptionDao(InheritanceAccount.class);
        List<InheritanceAccount> accountList = simpleDao.queryForAll();
        assertEquals(1, accountList.size());
        assertEquals(TEST_NAME_1, accountList.get(0).getName());
        assertEquals(TEST_ADDRESS_1, accountList.get(0).getAddress());
    }

    public void testOnDelete() {
        // given
        InheritanceAccount account = new InheritanceAccount(TEST_NAME_2, TEST_ADDRESS_2);
        RuntimeExceptionDao<InheritanceAccount, Integer> simpleDao = getHelper().getRuntimeExceptionDao(InheritanceAccount.class);
        simpleDao.create(account);
        List<InheritanceAccount> accountList = simpleDao.queryForAll();
        assertEquals(1, accountList.size());

        // when
        this.resolver.delete(InheritanceAccountContract.CONTENT_URI, BaseColumns._ID + " = " + account.getId(), null);

        // then
        accountList = simpleDao.queryForAll();
        assertEquals(0, accountList.size());
    }

    public void testOnUpdate() {
        // given
        InheritanceAccount account = new InheritanceAccount(TEST_NAME_1, TEST_ADDRESS_1);
        RuntimeExceptionDao<InheritanceAccount, Integer> simpleDao = getHelper().getRuntimeExceptionDao(InheritanceAccount.class);
        simpleDao.create(account);
        List<InheritanceAccount> accountList = simpleDao.queryForAll();
        assertEquals(1, accountList.size());

        ContentValues values = new ContentValues();
        values.clear();
        values.put(InheritanceAccountContract.NAME, TEST_NAME_2);
        values.put(InheritanceAccountContract.ADDRESS, TEST_ADDRESS_2);

        // when
        this.resolver.update(InheritanceAccountContract.CONTENT_URI, values, BaseColumns._ID + " = " + account.getId(), null);

        // then
        accountList = simpleDao.queryForAll();
        assertEquals(1, accountList.size());
        assertEquals(TEST_NAME_2, accountList.get(0).getName());
        assertEquals(TEST_ADDRESS_2, accountList.get(0).getAddress());
    }

    public void testOnQuery() {
        // given
        InheritanceAccount account1 = new InheritanceAccount(TEST_NAME_1, TEST_ADDRESS_1);
        InheritanceAccount account2 = new InheritanceAccount(TEST_NAME_2, TEST_ADDRESS_2);
        RuntimeExceptionDao<InheritanceAccount, Integer> simpleDao = getHelper().getRuntimeExceptionDao(InheritanceAccount.class);
        simpleDao.create(account1);
        simpleDao.create(account2);
        List<InheritanceAccount> accountList = simpleDao.queryForAll();
        assertEquals(2, accountList.size());

        // when
        Cursor cursor = this.resolver.query(InheritanceAccountContract.CONTENT_URI, new String[] {BaseColumns._ID, InheritanceAccountContract.NAME, InheritanceAccountContract.ADDRESS}, null, null, null);
        accountList = new ArrayList<InheritanceAccount>();
        while (cursor.moveToNext()) {
            InheritanceAccount account = new InheritanceAccount(cursor.getString(1), cursor.getString(2));
            accountList.add(account);
        }
        cursor.close();

        // then
        assertEquals(2, accountList.size());
        assertEquals(TEST_NAME_1, accountList.get(0).getName());
        assertEquals(TEST_ADDRESS_1, accountList.get(0).getAddress());
        assertEquals(TEST_NAME_2, accountList.get(1).getName());
        assertEquals(TEST_ADDRESS_2, accountList.get(1).getAddress());
    }

    public void testOnQueryWithOrder() {
        // given
        InheritanceAccount account1 = new InheritanceAccount(TEST_NAME_1, TEST_ADDRESS_1);
        InheritanceAccount account2 = new InheritanceAccount(TEST_NAME_2, TEST_ADDRESS_2);
        RuntimeExceptionDao<InheritanceAccount, Integer> simpleDao = getHelper().getRuntimeExceptionDao(InheritanceAccount.class);
        simpleDao.create(account1);
        simpleDao.create(account2);
        List<InheritanceAccount> accountList = simpleDao.queryForAll();
        assertEquals(2, accountList.size());

        // when
        String order = BaseColumns._ID + " DESC";
        Cursor cursor = this.resolver.query(InheritanceAccountContract.CONTENT_URI, new String[] {BaseColumns._ID, InheritanceAccountContract.NAME, InheritanceAccountContract.ADDRESS}, null, null, order);
        accountList = new ArrayList<InheritanceAccount>();
        while (cursor.moveToNext()) {
            InheritanceAccount account = new InheritanceAccount(cursor.getString(1), cursor.getString(2));
            accountList.add(account);
        }
        cursor.close();

        // then
        assertEquals(2, accountList.size());
        assertEquals(TEST_NAME_2, accountList.get(0).getName());
        assertEquals(TEST_ADDRESS_2, accountList.get(0).getAddress());
        assertEquals(TEST_NAME_1, accountList.get(1).getName());
        assertEquals(TEST_ADDRESS_1, accountList.get(1).getAddress());
    }

    public void testContentProviderAcquisition() throws RemoteException {
        // given
        InheritanceAccount account1 = new InheritanceAccount(TEST_NAME_1, TEST_ADDRESS_1);
        InheritanceAccount account2 = new InheritanceAccount(TEST_NAME_2, TEST_ADDRESS_2);
        RuntimeExceptionDao<InheritanceAccount, Integer> simpleDao = getHelper().getRuntimeExceptionDao(InheritanceAccount.class);
        simpleDao.create(account1);
        simpleDao.create(account2);
        List<InheritanceAccount> accountList = simpleDao.queryForAll();
        assertEquals(2, accountList.size());

        // when
        ContentProviderClient contentProviderClient = this.resolver.acquireContentProviderClient(InheritanceAccountContract.CONTENT_URI);
        Cursor cursor = contentProviderClient.query(InheritanceAccountContract.CONTENT_URI, null, null, null, null);

        // then
        accountList = new ArrayList<InheritanceAccount>();
        while (cursor.moveToNext()) {
            InheritanceAccount account = new InheritanceAccount(cursor.getString(1), cursor.getString(2));
            accountList.add(account);
        }
        cursor.close();
        contentProviderClient.release();

        assertEquals(2, accountList.size());
        assertEquals(TEST_NAME_1, accountList.get(0).getName());
        assertEquals(TEST_ADDRESS_1, accountList.get(0).getAddress());
        assertEquals(TEST_NAME_2, accountList.get(1).getName());
        assertEquals(TEST_ADDRESS_2, accountList.get(1).getAddress());
    }

    public void testBulkInsert() {
        // given
        final int testInheritanceAccountCount = 10;
        ContentValues[] contentValues = new ContentValues[testInheritanceAccountCount];
        for (int accountIndex = 0; accountIndex < testInheritanceAccountCount; accountIndex++) {
            ContentValues values = new ContentValues();
            values.clear();
            values.put(InheritanceAccountContract.NAME, TEST_NAME_1 + accountIndex);
            values.put(InheritanceAccountContract.ADDRESS, TEST_ADDRESS_1 + accountIndex);
            contentValues[accountIndex] = values;
        }
        // when
        this.resolver.bulkInsert(InheritanceAccountContract.CONTENT_URI, contentValues);

        // then
        RuntimeExceptionDao<InheritanceAccount, Integer> simpleDao = getHelper().getRuntimeExceptionDao(InheritanceAccount.class);
        List<InheritanceAccount> accountList = simpleDao.queryForAll();
        assertEquals(testInheritanceAccountCount, accountList.size());
        int accountIndex = 0;
        for (InheritanceAccount account : accountList) {
            assertEquals(TEST_NAME_1 + accountIndex++, account.getName());
            assertEquals(TEST_ADDRESS_1 + accountIndex++, account.getAddress());
        }
    }

    public void testApplyBatch() throws RemoteException, OperationApplicationException {
        // given
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
        operations.add(
                ContentProviderOperation.newInsert(InheritanceAccountContract.CONTENT_URI)
                        .withValue(InheritanceAccountContract.NAME, TEST_NAME_1)
                        .withValue(InheritanceAccountContract.ADDRESS, TEST_ADDRESS_1)
                        .build());
        operations.add(
                ContentProviderOperation.newInsert(InheritanceAccountContract.CONTENT_URI)
                        .withValue(InheritanceAccountContract.NAME, TEST_NAME_2)
                        .withValue(InheritanceAccountContract.ADDRESS, TEST_ADDRESS_2)
                        .build());

        // when
        this.resolver.applyBatch(InheritanceAccountContract.AUTHORITY, operations);

        // then
        RuntimeExceptionDao<InheritanceAccount, Integer> simpleDao = getHelper().getRuntimeExceptionDao(InheritanceAccount.class);
        List<InheritanceAccount> accountList = simpleDao.queryForAll();
        assertEquals(2, accountList.size());
        assertEquals(TEST_NAME_1, accountList.get(0).getName());
        assertEquals(TEST_ADDRESS_1, accountList.get(0).getAddress());
        assertEquals(TEST_NAME_2, accountList.get(1).getName());
        assertEquals(TEST_ADDRESS_2, accountList.get(1).getAddress());
    }

    private SampleHelper getHelper() {
        return new SampleHelper(getInstrumentation().getTargetContext());
    }
}
