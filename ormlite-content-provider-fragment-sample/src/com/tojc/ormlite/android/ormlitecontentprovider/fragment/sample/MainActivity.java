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
package com.tojc.ormlite.android.ormlitecontentprovider.fragment.sample;

import android.app.Activity;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;

import com.tojc.ormlite.android.ormlitecontentprovider.fragment.sample.provider.AccountContract;
import com.tojc.ormlite.android.ormlitecontentprovider.fragment.sample.provider.BlockContract;
import com.tojc.ormlite.android.ormlitecontentprovider.fragment.sample.provider.CarContract;

import java.util.ArrayList;

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
        c.moveToFirst();
        do {
            for (int i = 0; i < c.getColumnCount(); i++) {
                Log.d(getClass().getSimpleName(), c.getColumnName(i) + " : " + c.getString(i));
            }
        } while (c.moveToNext());
        c.close();

        // select test
        Log.d("", "test car start");
        Cursor ccar = getContentResolver().query(CarContract.CONTENT_URI, null, null, null, null);
        ccar.moveToFirst();
        do {
            for (int i = 0; i < ccar.getColumnCount(); i++) {
                Log.d(getClass().getSimpleName(), ccar.getColumnName(i) + " : " + ccar.getString(i));
            }
        } while (ccar.moveToNext());
        ccar.close();
        Log.d("", "test car end");

        // select test
        Log.d("", "test block start");
        Cursor cblock = getContentResolver().query(BlockContract.buildBlockUri(1), null, null, null, null);
        cblock.moveToFirst();
        do {
            for (int i = 0; i < cblock.getColumnCount(); i++) {
                Log.d(getClass().getSimpleName(), cblock.getColumnName(i) + " : " + cblock.getString(i));
            }
        } while (cblock.moveToNext());
        cblock.close();

        Log.d("", "test block step1");

        Cursor cblock2 = getContentResolver().query(BlockContract.buildBlockNameUri("ma"), null, null, null, null);
        cblock2.moveToFirst();
        do {
            for (int i = 0; i < cblock2.getColumnCount(); i++) {
                Log.d(getClass().getSimpleName(), cblock2.getColumnName(i) + " : " + cblock2.getString(i));
            }
        } while (cblock2.moveToNext());
        cblock2.close();

        Log.d("", "test block step2: left start");

        Cursor cblock3 = getContentResolver().query(BlockContract.buildBlockLNameUri("ma"), null, null, null, null);
        cblock3.moveToFirst();
        do {
            for (int i = 0; i < cblock3.getColumnCount(); i++) {
                Log.d(getClass().getSimpleName(), cblock3.getColumnName(i) + " : " + cblock3.getString(i));
            }
        } while (cblock3.moveToNext());
        cblock3.close();

        Log.d("", "test block step3: right start");

        Cursor cblock4 = getContentResolver().query(BlockContract.buildBlockRNameUri("ma"), null, null, null, null);
        cblock4.moveToFirst();
        do {
            for (int i = 0; i < cblock4.getColumnCount(); i++) {
                Log.d(getClass().getSimpleName(), cblock4.getColumnName(i) + " : " + cblock4.getString(i));
            }
        } while (cblock4.moveToNext());
        cblock4.close();

        Log.d("", "test block end");

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
            c2.moveToFirst();
            do {
                for (int i = 0; i < c2.getColumnCount(); i++) {
                    Log.d(getClass().getSimpleName(), c2.getColumnName(i) + " : " + c2.getString(i));
                }
            } while (c2.moveToNext());
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
