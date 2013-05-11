package com.tojc.ormlite.android.ormlitecontentprovidersample;

import java.util.ArrayList;

import com.tojc.ormlite.android.ormlitecontentprovidersample.provider.Contract;

import android.os.Bundle;
import android.os.RemoteException;
import android.app.Activity;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// insert test
		ContentValues values = new ContentValues();
		values.clear();
		values.put(Contract.Account.NAME, "Yamada Tarou");
		getContentResolver().insert(Contract.Account.contentUri, values);

		// bulkInsert test
		int length = 10;
		ContentValues[] contentValues = new ContentValues[length];
		for(int i = 0; i < length; i++)
		{
			values = new ContentValues();
			values.clear();
			values.put(Contract.Account.NAME, "Yamada Tarou: " + i);
			contentValues[i] = values;
		}
		getContentResolver().bulkInsert(Contract.Account.contentUri, contentValues);

		// select test
		Cursor c = getContentResolver().query(Contract.Account.contentUri, null, null, null, null);
		while(c.moveToNext())
		{
			for (int i = 0; i < c.getColumnCount(); i++)
			{
				Log.d(getClass().getSimpleName(), c.getColumnName(i) + " : " + c.getString(i));
			}
		}
		c.close();

		// applyBatch test
		ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
		operations.add(ContentProviderOperation.newInsert(Contract.Account.contentUri)
				.withValue(Contract.Account.NAME, "Yamada Hanako 1").build());
		operations.add(ContentProviderOperation.newInsert(Contract.Account.contentUri)
				.withValue(Contract.Account.NAME, "Yamada Hanako 2").build());
		try
		{
			getContentResolver().applyBatch(Contract.AUTHORITY, operations);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		// ContentProviderClient test
		ContentProviderClient client = getContentResolver().acquireContentProviderClient(Contract.Account.contentUri);
		Cursor c2 = null;
		try
		{
			c2 = client.query(Contract.Account.contentUri, null, null, null, null);
		}
		catch(RemoteException e)
		{
			e.printStackTrace();
		}
		while(c2.moveToNext())
		{
			for (int i = 0; i < c2.getColumnCount(); i++)
			{
				Log.d(getClass().getSimpleName(), c2.getColumnName(i) + " : " + c2.getString(i));
			}
		}
		c2.close();
		client.release();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
