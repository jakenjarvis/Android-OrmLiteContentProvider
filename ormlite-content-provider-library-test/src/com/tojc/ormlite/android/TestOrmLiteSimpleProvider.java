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
import com.tojc.ormlite.android.test.model.Account;
import com.tojc.ormlite.android.test.provider.AccountContract;
import com.tojc.ormlite.android.test.provider.SampleHelper;
import com.tojc.ormlite.android.test.provider.UnderTestSampleProvider;

@MediumTest
public class TestOrmLiteSimpleProvider extends InstrumentationTestCase {

    private static final String TEST_NAME_1 = "Yamada Tarou";
    private static final String TEST_NAME_2 = "Stephane Nicolas";

    private MockContentResolver resolver;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getHelper().resetAllTables();

        UnderTestSampleProvider provider = new UnderTestSampleProvider();
        provider.attachInfo(getInstrumentation().getContext(), null);

        this.resolver = new MockContentResolver();
        this.resolver.addProvider(AccountContract.AUTHORITY, provider);
    }

    public void testOnInsert() {
        // given
        ContentValues values = new ContentValues();
        values.clear();
        values.put(AccountContract.NAME, TEST_NAME_1);

        // when
        this.resolver.insert(AccountContract.CONTENT_URI, values);

        // then
        RuntimeExceptionDao<Account, Integer> simpleDao = getHelper().getRuntimeExceptionDao(Account.class);
        List<Account> accountList = simpleDao.queryForAll();
        assertEquals(1, accountList.size());
        assertEquals(TEST_NAME_1, accountList.get(0).getName());
    }

    public void testOnDelete() {
        // given
        Account account = new Account(TEST_NAME_2);
        RuntimeExceptionDao<Account, Integer> simpleDao = getHelper().getRuntimeExceptionDao(Account.class);
        simpleDao.create(account);
        List<Account> accountList = simpleDao.queryForAll();
        assertEquals(1, accountList.size());

        // when
        this.resolver.delete(AccountContract.CONTENT_URI, BaseColumns._ID + " = " + account.getId(), null);

        // then
        accountList = simpleDao.queryForAll();
        assertEquals(0, accountList.size());
    }

    public void testOnUpdate() {
        // given
        Account account = new Account(TEST_NAME_1);
        RuntimeExceptionDao<Account, Integer> simpleDao = getHelper().getRuntimeExceptionDao(Account.class);
        simpleDao.create(account);
        List<Account> accountList = simpleDao.queryForAll();
        assertEquals(1, accountList.size());

        ContentValues values = new ContentValues();
        values.clear();
        values.put(AccountContract.NAME, TEST_NAME_2);

        // when
        this.resolver.update(AccountContract.CONTENT_URI, values, BaseColumns._ID + " = " + account.getId(), null);

        // then
        accountList = simpleDao.queryForAll();
        assertEquals(1, accountList.size());
        assertEquals(TEST_NAME_2, accountList.get(0).getName());
    }

    public void testOnQuery() {
        // given
        Account account1 = new Account(TEST_NAME_1);
        Account account2 = new Account(TEST_NAME_2);
        RuntimeExceptionDao<Account, Integer> simpleDao = getHelper().getRuntimeExceptionDao(Account.class);
        simpleDao.create(account1);
        simpleDao.create(account2);
        List<Account> accountList = simpleDao.queryForAll();
        assertEquals(2, accountList.size());

        // when
        Cursor cursor = this.resolver.query(AccountContract.CONTENT_URI, new String[] {BaseColumns._ID, AccountContract.NAME}, null, null, null);
        accountList = new ArrayList<Account>();
        while (cursor.moveToNext()) {
            Account account = new Account(cursor.getString(1));
            accountList.add(account);
        }
        cursor.close();

        // then
        assertEquals(2, accountList.size());
        assertEquals(TEST_NAME_1, accountList.get(0).getName());
        assertEquals(TEST_NAME_2, accountList.get(1).getName());
    }

    public void testOnQueryWithOrder() {
        // given
        Account account1 = new Account(TEST_NAME_1);
        Account account2 = new Account(TEST_NAME_2);
        RuntimeExceptionDao<Account, Integer> simpleDao = getHelper().getRuntimeExceptionDao(Account.class);
        simpleDao.create(account1);
        simpleDao.create(account2);
        List<Account> accountList = simpleDao.queryForAll();
        assertEquals(2, accountList.size());

        // when
        String order = BaseColumns._ID + " DESC";
        Cursor cursor = this.resolver.query(AccountContract.CONTENT_URI, new String[] {BaseColumns._ID, AccountContract.NAME}, null, null, order);
        accountList = new ArrayList<Account>();
        while (cursor.moveToNext()) {
            Account account = new Account(cursor.getString(1));
            accountList.add(account);
        }
        cursor.close();

        // then
        assertEquals(2, accountList.size());
        assertEquals(TEST_NAME_2, accountList.get(0).getName());
        assertEquals(TEST_NAME_1, accountList.get(1).getName());
    }

    public void testContentProviderAcquisition() throws RemoteException {
        // given
        Account account1 = new Account(TEST_NAME_1);
        Account account2 = new Account(TEST_NAME_2);
        RuntimeExceptionDao<Account, Integer> simpleDao = getHelper().getRuntimeExceptionDao(Account.class);
        simpleDao.create(account1);
        simpleDao.create(account2);
        List<Account> accountList = simpleDao.queryForAll();
        assertEquals(2, accountList.size());

        // when
        ContentProviderClient contentProviderClient = this.resolver.acquireContentProviderClient(AccountContract.CONTENT_URI);
        Cursor cursor = contentProviderClient.query(AccountContract.CONTENT_URI, null, null, null, null);

        // then
        accountList = new ArrayList<Account>();
        while (cursor.moveToNext()) {
            Account account = new Account(cursor.getString(1));
            accountList.add(account);
        }
        cursor.close();
        contentProviderClient.release();

        assertEquals(2, accountList.size());
        assertEquals(TEST_NAME_1, accountList.get(0).getName());
        assertEquals(TEST_NAME_2, accountList.get(1).getName());
    }

    public void testBulkInsert() {
        // given
        final int testAccountCount = 10;
        ContentValues[] contentValues = new ContentValues[testAccountCount];
        for (int accountIndex = 0; accountIndex < testAccountCount; accountIndex++) {
            ContentValues values = new ContentValues();
            values.clear();
            values.put(AccountContract.NAME, TEST_NAME_1 + accountIndex);
            contentValues[accountIndex] = values;
        }
        // when
        this.resolver.bulkInsert(AccountContract.CONTENT_URI, contentValues);

        // then
        RuntimeExceptionDao<Account, Integer> simpleDao = getHelper().getRuntimeExceptionDao(Account.class);
        List<Account> accountList = simpleDao.queryForAll();
        assertEquals(testAccountCount, accountList.size());
        int accountIndex = 0;
        for (Account account : accountList) {
            assertEquals(TEST_NAME_1 + accountIndex++, account.getName());
        }
    }

    public void testApplyBatch() throws RemoteException, OperationApplicationException {
        // given
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
        operations.add(ContentProviderOperation.newInsert(AccountContract.CONTENT_URI).withValue(AccountContract.NAME, TEST_NAME_1).build());
        operations.add(ContentProviderOperation.newInsert(AccountContract.CONTENT_URI).withValue(AccountContract.NAME, TEST_NAME_2).build());

        // when
        this.resolver.applyBatch(AccountContract.AUTHORITY, operations);

        // then
        RuntimeExceptionDao<Account, Integer> simpleDao = getHelper().getRuntimeExceptionDao(Account.class);
        List<Account> accountList = simpleDao.queryForAll();
        assertEquals(2, accountList.size());
        assertEquals(TEST_NAME_1, accountList.get(0).getName());
        assertEquals(TEST_NAME_2, accountList.get(1).getName());
    }

    private SampleHelper getHelper() {
        return new SampleHelper(getInstrumentation().getTargetContext());
    }
}
