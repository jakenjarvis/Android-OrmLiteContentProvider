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
package com.tojc.ormlite.android.ormlitecontentprovider.compiler.sample;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;

import com.tojc.ormlite.android.ormlitecontentprovider.compiler.sample.model.AccountContract;
import com.tojc.ormlite.android.ormlitecontentprovider.compiler.sample.model.CarContract;
import com.tojc.ormlite.android.ormlitecontentprovider.compiler.sample.model.Fuel;
import com.tojc.ormlite.android.ormlitecontentprovider.compiler.sample.model.FuelContract;

public class MainActivity extends Activity {
    private static final int TEST_ENTRY_COUNT = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // insert test
        ContentValues values = new ContentValues();
        values.clear();
        values.put(AccountContract.NAME, "Yamada Tarou");
        getContentResolver().insert(AccountContract.CONTENT_URI, values);

        // bulkInsert test
        ContentValues[] contentValues = new ContentValues[TEST_ENTRY_COUNT];
        for (int i = 0; i < TEST_ENTRY_COUNT; i++) {
            values = new ContentValues();
            values.clear();
            values.put(AccountContract.NAME, "Yamada Tarou: " + i);
            contentValues[i] = values;
        }
        getContentResolver().bulkInsert(AccountContract.CONTENT_URI, contentValues);

        // select test
        Cursor c = getContentResolver().query(AccountContract.CONTENT_URI, null, null, null, null);
        while (c.moveToNext()) {
            for (int i = 0; i < c.getColumnCount(); i++) {
                Log.d(getClass().getSimpleName(), c.getColumnName(i) + " : " + c.getString(i));
            }
        }
        c.close();

        //
        Cursor cz1 = getContentResolver().query(CarContract.CONTENT_URI, null, null, null, null);
        while (cz1.moveToNext()) {
            for (int i = 0; i < cz1.getColumnCount(); i++) {
                Log.d(getClass().getSimpleName(), cz1.getColumnName(i) + " : " + cz1.getString(i));
            }
        }
        cz1.close();

        // applyBatch test
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
        operations.add(ContentProviderOperation.newInsert(AccountContract.CONTENT_URI).withValue(AccountContract.NAME, "Yamada Hanako 1").build());
        operations.add(ContentProviderOperation.newInsert(AccountContract.CONTENT_URI).withValue(AccountContract.NAME, "Yamada Hanako 2").build());
        try {
            getContentResolver().applyBatch(AccountContract.AUTHORITY, operations);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ContentProviderClient test
        ContentProviderClient client = getContentResolver().acquireContentProviderClient(AccountContract.CONTENT_URI);
        Cursor c2 = null;
        try {
            c2 = client.query(AccountContract.CONTENT_URI, null, null, null, null);
            while (c2.moveToNext()) {
                for (int i = 0; i < c2.getColumnCount(); i++) {
                    Log.d(getClass().getSimpleName(), c2.getColumnName(i) + " : " + c2.getString(i));
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            if (c2 != null) {
                c2.close();
            }
        }
        client.release();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

}
