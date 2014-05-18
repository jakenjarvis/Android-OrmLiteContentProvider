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

import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.test.mock.MockContentResolver;
import android.test.suitebuilder.annotation.MediumTest;

import com.tojc.ormlite.android.test.provider.AccountContract;
import com.tojc.ormlite.android.test.provider.UnderTestSampleProvider;

// TODO: This test class is cutting corners. Need to modify them.
@MediumTest
public class TestOrmLiteSimpleProviderCase3 extends OrmLiteSimpleProviderTestBase {
    protected static final String TEST_FRAGMENT_KEY_NAME1 = "UnderTestSampleFragment1";
    protected static final String TEST_FRAGMENT_KEY_NAME2 = "UnderTestSampleFragment2";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getHelper().resetAllTables();

        this.provider = new UnderTestSampleProvider(3);
        this.provider.attachInfo(getInstrumentation().getContext(), null);
        assertEquals(this.provider.getContentProviderFragments().size(), 2);

        this.resolver = new MockContentResolver();
        this.resolver.addProvider(AccountContract.AUTHORITY, provider);
    }

    public void testOnInsert() {
        super.functionOnInsert();
        assertEquals(this.getContentProviderFragment(TEST_FRAGMENT_KEY_NAME1).getEventClassesList().size(), 2);
        assertEquals(this.getContentProviderFragment(TEST_FRAGMENT_KEY_NAME2).getEventClassesList().size(), 0);
    }

    public void testOnDelete() {
        super.functionOnDelete();
        assertEquals(this.getContentProviderFragment(TEST_FRAGMENT_KEY_NAME1).getEventClassesList().size(), 2);
        assertEquals(this.getContentProviderFragment(TEST_FRAGMENT_KEY_NAME2).getEventClassesList().size(), 0);
    }

    public void testOnUpdate() {
        super.functionOnUpdate();
        assertEquals(this.getContentProviderFragment(TEST_FRAGMENT_KEY_NAME1).getEventClassesList().size(), 2);
        assertEquals(this.getContentProviderFragment(TEST_FRAGMENT_KEY_NAME2).getEventClassesList().size(), 0);
    }

    public void testOnQuery() {
        super.functionOnQuery();
        assertEquals(this.getContentProviderFragment(TEST_FRAGMENT_KEY_NAME1).getEventClassesList().size(), 2);
        assertEquals(this.getContentProviderFragment(TEST_FRAGMENT_KEY_NAME2).getEventClassesList().size(), 0);
    }

    public void testOnQueryWithOrder() {
        super.functionOnQueryWithOrder();
        assertEquals(this.getContentProviderFragment(TEST_FRAGMENT_KEY_NAME1).getEventClassesList().size(), 2);
        assertEquals(this.getContentProviderFragment(TEST_FRAGMENT_KEY_NAME2).getEventClassesList().size(), 0);
    }

    public void testContentProviderAcquisition() throws RemoteException {
        super.functionContentProviderAcquisition();
        assertEquals(this.getContentProviderFragment(TEST_FRAGMENT_KEY_NAME1).getEventClassesList().size(), 2);
        assertEquals(this.getContentProviderFragment(TEST_FRAGMENT_KEY_NAME2).getEventClassesList().size(), 0);
    }

    public void testBulkInsert() {
        super.functionBulkInsert();
        assertEquals(this.getContentProviderFragment(TEST_FRAGMENT_KEY_NAME1).getEventClassesList().size(), 13);
        assertEquals(this.getContentProviderFragment(TEST_FRAGMENT_KEY_NAME2).getEventClassesList().size(), 0);
    }

    public void testApplyBatch() throws RemoteException, OperationApplicationException {
        super.functionApplyBatch();
        assertEquals(this.getContentProviderFragment(TEST_FRAGMENT_KEY_NAME1).getEventClassesList().size(), 6);
        assertEquals(this.getContentProviderFragment(TEST_FRAGMENT_KEY_NAME2).getEventClassesList().size(), 2);
    }
}
