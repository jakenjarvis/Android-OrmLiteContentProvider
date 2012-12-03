package com.tojc.ormlite.android.ormlitecontentprovidersample;

import com.tojc.ormlite.android.ormlitecontentprovidersample.provider.Contract;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
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
		
		ContentValues values = new ContentValues();
		values.clear();
		values.put(Contract.Account.NAME, "Yamada Tarou");
		getContentResolver().insert(Contract.Account.contentUri, values);
		
		Cursor c = getContentResolver().query(Contract.Account.contentUri, null, null, null, null);
		while(c.moveToNext())
		{
			for (int i = 0; i < c.getColumnCount(); i++)
			{
				Log.d(getClass().getSimpleName(), c.getColumnName(i) + " : " + c.getString(i));
			}
		}
		c.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
