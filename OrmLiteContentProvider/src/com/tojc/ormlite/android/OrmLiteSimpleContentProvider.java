/*
 * This file is part of the Android-OrmLiteContentProvider package.
 * 
 * Copyright (c) 2012, Jaken Jarvis (jaken.jarvis@gmail.com)
 * 
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 * 
 * The author may be contacted via 
 * https://github.com/jakenjarvis/Android-OrmLiteContentProvider
 */
package com.tojc.ormlite.android;

import android.content.ContentUris;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.tojc.ormlite.android.framework.MatcherPattern;
import com.tojc.ormlite.android.framework.OperationParameters.DeleteParameters;
import com.tojc.ormlite.android.framework.OperationParameters.InsertParameters;
import com.tojc.ormlite.android.framework.OperationParameters.QueryParameters;
import com.tojc.ormlite.android.framework.OperationParameters.UpdateParameters;

/**
 * This is a simple class that utilizes the framework. You can make ContentProvider minimal implementation.
 * This is an example of how to implement OrmLiteDefaultContentProvider.
 * 
 * @author Jaken
 */
public abstract class OrmLiteSimpleContentProvider<T extends OrmLiteSqliteOpenHelper> extends OrmLiteDefaultContentProvider<T>
{
	/*
	 * @see com.tojc.ormlite.android.OrmLiteDefaultContentProvider#onQuery(com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper, com.tojc.ormlite.android.framework.MatcherPattern, com.tojc.ormlite.android.framework.OperationParameters.QueryParameters)
	 */
	@Override
	public Cursor onQuery(T helper, MatcherPattern target, QueryParameters parameter)
	{
		Cursor result = null;

		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(target.getTableInfo().getName());
		builder.setProjectionMap(target.getTableInfo().getProjectionMap());

		// where
		switch(target.getMimeTypeVnd().getSubType())
		{
			case Directory:
				break;

			case Item:
				String where = target.getTableInfo().getIdColumnInfo().getColumnName()
						+ "="
						+ parameter.getUri().getPathSegments().get(1);
				if((parameter.getSelection() != null) && (parameter.getSelection().length() >= 1))
				{
					where += " AND ( " + parameter.getSelection() + " ) ";
				}
				builder.appendWhere(where);
				break;
		}

		// orderBy
		String orderBy = "";
		if((parameter.getSortOrder() != null) && (parameter.getSortOrder().length() >= 1))
		{
			orderBy = parameter.getSortOrder();
		}
		else
		{
			orderBy = target.getTableInfo().getDefaultSortOrderString();
		}

		SQLiteDatabase db = helper.getReadableDatabase();

		result = builder.query(db, parameter.getProjection(), parameter.getSelection(), parameter.getSelectionArgs(), null, null, orderBy);
		return result;
	}

	/*
	 * @see com.tojc.ormlite.android.OrmLiteDefaultContentProvider#onInsert(com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper, com.tojc.ormlite.android.framework.MatcherPattern, com.tojc.ormlite.android.framework.OperationParameters.InsertParameters)
	 */
	@Override
	public Uri onInsert(T helper, MatcherPattern target, InsertParameters parameter)
	{
		Uri result = null;
		
		SQLiteDatabase db = helper.getWritableDatabase();

		long id = db.insert(target.getTableInfo().getName(), null, parameter.getValues());
		if(id >= 0)
		{
			result = ContentUris.withAppendedId(target.getContentUriPattern(), id);
		}
		else
		{
			throw new SQLException("Failed to insert row into : " + parameter.getUri().toString());
		}
		return result;
	}

	/*
	 * @see com.tojc.ormlite.android.OrmLiteDefaultContentProvider#onDelete(com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper, com.tojc.ormlite.android.framework.MatcherPattern, com.tojc.ormlite.android.framework.OperationParameters.DeleteParameters)
	 */
	@Override
	public int onDelete(T helper, MatcherPattern target, DeleteParameters parameter)
	{
		int result = -1;

		SQLiteDatabase db = helper.getWritableDatabase();

		switch(target.getMimeTypeVnd().getSubType())
		{
			case Directory:
				result = db.delete(target.getTableInfo().getName(), parameter.getSelection(), parameter.getSelectionArgs());
				break;

			case Item:
				String where = target.getTableInfo().getIdColumnInfo().getColumnName()
						+ "="
						+ parameter.getUri().getPathSegments().get(1);
				if((parameter.getSelection() != null) && (parameter.getSelection().length() >= 1))
				{
					where += " AND ( " + parameter.getSelection() + " ) ";
				}
				result = db.delete(target.getTableInfo().getName(), where, parameter.getSelectionArgs());
				break;
		}
		return result;
	}

	/*
	 * @see com.tojc.ormlite.android.OrmLiteDefaultContentProvider#onUpdate(com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper, com.tojc.ormlite.android.framework.MatcherPattern, com.tojc.ormlite.android.framework.OperationParameters.UpdateParameters)
	 */
	@Override
	public int onUpdate(T helper, MatcherPattern target, UpdateParameters parameter)
	{
		int result = -1;

		SQLiteDatabase db = helper.getWritableDatabase();

		switch(target.getMimeTypeVnd().getSubType())
		{
			case Directory:
				result = db.update(target.getTableInfo().getName(), parameter.getValues(), parameter.getSelection(), parameter.getSelectionArgs());
				break;

			case Item:
				String where = target.getTableInfo().getIdColumnInfo().getColumnName()
						+ "="
						+ parameter.getUri().getPathSegments().get(1);
				if((parameter.getSelection() != null) && (parameter.getSelection().length() >= 1))
				{
					where += " AND ( " + parameter.getSelection() + " ) ";
				}
				result = db.update(target.getTableInfo().getName(), parameter.getValues(), where, parameter.getSelectionArgs());
				break;
		}
		return result;
	}

}
