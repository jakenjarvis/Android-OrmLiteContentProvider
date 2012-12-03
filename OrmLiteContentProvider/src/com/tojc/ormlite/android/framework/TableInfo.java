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
package com.tojc.ormlite.android.framework;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import android.provider.BaseColumns;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.ContentMimeTypeVndInfo;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.ContentUriInfo;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.SortOrderInfo;
import com.tojc.ormlite.android.annotation.OrmLiteAnnotationAccessor;

/**
 * Manage the database table information.
 * 
 * @author Jaken
 */
public class TableInfo implements Validity
{
	private Class<?> classType;
	private String name;

	private ContentUriInfo defaultContentUriInfo;
	private ContentMimeTypeVndInfo defaultContentMimeTypeVndInfo;

	private Map<String, ColumnInfo> columns = null;
	private Map<String, String> projectionMap = null;
	
	private ColumnInfo idColumnInfo = null;
	
	private String defaultSortOrder = "";
	
	public TableInfo(Class<?> tableClassType)
	{
		if(!tableClassType.isAnnotationPresent(DatabaseTable.class))
		{
			throw new IllegalArgumentException("Parameter does not implement the DatabaseTable annotation.");
		}
		if(!(tableClassType instanceof Class<?>))
		{
			throw new IllegalArgumentException("Parameter is not a Class<?>.");
		}

		this.classType = tableClassType;
		this.name = OrmLiteAnnotationAccessor.getAnnotationTableName(tableClassType);

		this.defaultContentUriInfo = new ContentUriInfo(tableClassType);
		this.defaultContentMimeTypeVndInfo = new ContentMimeTypeVndInfo(tableClassType);
		
		this.columns = new HashMap<String, ColumnInfo>();
		this.projectionMap = new HashMap<String, String>();

		SortedMap<Integer, String> defaultSortOrderMap = new TreeMap<Integer, String>();

		this.idColumnInfo = null;
		for(Field classfield : tableClassType.getDeclaredFields())
		{
			if(classfield.isAnnotationPresent(DatabaseField.class))
			{
				classfield.setAccessible(true);	// private field accessible
				
				ColumnInfo columnInfo = new ColumnInfo(classfield);
				this.columns.put(columnInfo.getColumnName() ,columnInfo);
				
				// check id
				if(columnInfo.getColumnName().equals(BaseColumns._ID))
				{
					boolean generatedId = classfield.getAnnotation(DatabaseField.class).generatedId();
					if(generatedId)
					{
						this.idColumnInfo = columnInfo;
					}
				}
				
				// DefaultSortOrder
				SortOrderInfo defaultSortOrderInfo = columnInfo.getDefaultSortOrderInfo();
				if(defaultSortOrderInfo.isValid())
				{
					defaultSortOrderMap.put(
						defaultSortOrderInfo.getWeight(),
						defaultSortOrderInfo.makeSqlOrderString(columnInfo.getColumnName()));
				}
				
				// ProjectionMap
				this.projectionMap.put(columnInfo.getProjectionColumnName(), columnInfo.getColumnName());

			}
		}

		if(this.idColumnInfo == null)
		{
			// @DatabaseField(columnName = _ID, generatedId = true)
			// private int _id;
			throw new IllegalArgumentException("Proper ID is not defined for field.");
		}
		
		// DefaultSortOrder
		if(defaultSortOrderMap.size() >= 1)
		{
			// make SQL OrderBy
			StringBuilder result = new StringBuilder();
			String comma = "";
			for (Map.Entry<Integer, String> entry : defaultSortOrderMap.entrySet())
			{
				result.append(comma);
				result.append(entry.getValue());
				comma = ", ";
			}
			this.defaultSortOrder = result.toString();
		}
		else
		{
			this.defaultSortOrder = "";
		}
	}

	@Override
	public boolean isValid()
	{
		return isValid(false);
	}

	@Override
	public boolean isValid(boolean throwException)
	{
		boolean result = true;

		if(this.classType == null)
		{
			result = false;
			if(throwException && !result)
			{
				throw new IllegalStateException("classType is null.");
			}
		}
		else if(this.name.length() <= 0)
		{
			result = false;
			if(throwException && !result)
			{
				throw new IllegalStateException("name is zero string.");
			}
		}
		// Acceptable
		//		else if(!this.defaultContentUriInfo.isValid())
		//		{
		//			result = false;
		//		}
		// Acceptable
		//		else if(!this.defaultContentMimeTypeVndInfo.isValid())
		//		{
		//			result = false;
		//		}
		else if(this.columns.size() <= 0)
		{
			result = false;
			if(throwException && !result)
			{
				throw new IllegalStateException("columns is zero size.");
			}
		}
		else if(this.columns.size() != this.projectionMap.size())
		{
			result = false;
			if(throwException && !result)
			{
				throw new IllegalStateException("Number of columns and projectionMap do not match.");
			}
		}
		// Acceptable
		//		else if(this.defaultSortOrder.length() <= 0)
		//		{
		//			result = false;
		//		}
		
		for (Map.Entry<String, ColumnInfo> entry : this.columns.entrySet())
		{
			result = result && entry.getValue().isValid(throwException);
		}

		return result;
	}

	public Class<?> getClassType()
	{
		return this.classType;
	}
	
	public String getName()
	{
		return this.name;
	}

	/**
	 * @see com.tojc.ormlite.android.framework.MatcherPattern#getContentUriInfo()
	 * @return Gets the default value that is specified in the annotation.
	 *         If you want to know the state to match the pattern, see MatcherPattern#getContentUriInfo.
	 */
	public ContentUriInfo getDefaultContentUriInfo()
	{
		return this.defaultContentUriInfo;
	}

	/**
	 * @see com.tojc.ormlite.android.framework.MatcherPattern#getMimeTypeVnd()
	 * @return Gets the default value that is specified in the annotation.
	 *         If you want to know the state to match the pattern, see MatcherPattern#getMimeTypeVnd.
	 */
	public ContentMimeTypeVndInfo getDefaultContentMimeTypeVndInfo()
	{
		return this.defaultContentMimeTypeVndInfo;
	}

	/**
	 * @return Gets the default value that is specified in the annotation.
	 *         If you are specifying multiple fields, return the concatenated string.
	 *         ex) "timestamp DESC, _id ASC"
	 */
	public String getDefaultSortOrderString()
	{
		return this.defaultSortOrder;
	}
	
	/**
	 * @return Get column information for column "_id".
	 */
	public ColumnInfo getIdColumnInfo()
	{
		return this.idColumnInfo;
	}

	/**
	 * @see com.tojc.ormlite.android.annotation.AdditionalAnnotation.ProjectionMap
	 * @return Return to generate the ProjectionMap.
	 *         If you are not using the ProjectionMap annotations need not be used.
	 *         The Map is includes all columns.
	 */
	public Map<String, String> getProjectionMap()
	{
		return this.projectionMap;
	}

}
