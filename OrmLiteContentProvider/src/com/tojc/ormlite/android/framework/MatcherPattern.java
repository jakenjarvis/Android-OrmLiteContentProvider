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

import com.tojc.ormlite.android.annotation.AdditionalAnnotation.ContentMimeTypeVndInfo;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.ContentUriInfo;
import com.tojc.ormlite.android.framework.MimeTypeVnd.SubType;

import android.net.Uri;

/**
 * Manage the UriMatcher pattern. It holds information related to the pattern code.
 * 
 * @author Jaken
 */
public class MatcherPattern implements Validity
{
	private boolean preinitialized = false;

	private TableInfo tableInfo;
	private SubType subType;
	private String pattern;
	private int patternCode;

	private ContentUriInfo contentUriInfo;
	private ContentMimeTypeVndInfo contentMimeTypeVndInfo;

	private MimeTypeVnd mimeTypeVnd;

	public MatcherPattern(TableInfo tableInfo, SubType subType, String pattern, int patternCode)
	{
		this.tableInfo = tableInfo;
		this.subType = subType;
		this.pattern = pattern;
		this.patternCode = patternCode;
		
		if(this.tableInfo.getDefaultContentUriInfo().isValid())
		{
			this.contentUriInfo = this.tableInfo.getDefaultContentUriInfo();
		}
		else
		{
			this.contentUriInfo = null;
		}
		
		if(this.tableInfo.getDefaultContentMimeTypeVndInfo().isValid())
		{
			this.contentMimeTypeVndInfo = this.tableInfo.getDefaultContentMimeTypeVndInfo();
		}
		else
		{
			this.contentMimeTypeVndInfo = null;
		}
		
		if(this.contentMimeTypeVndInfo != null)
		{
			this.mimeTypeVnd = new MimeTypeVnd(this.subType, this.contentMimeTypeVndInfo);
		}
		else
		{
			this.mimeTypeVnd = null;
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
		
		if(this.tableInfo == null)
		{
			result = false;
			if(throwException && !result)
			{
				throw new IllegalStateException("tableInfo is null.");
			}
		}
		else if(this.subType == null)
		{
			result = false;
			if(throwException && !result)
			{
				throw new IllegalStateException("subType is null.");
			}
		}
		else if(this.pattern.length() <= 0)
		{
			result = false;
			if(throwException && !result)
			{
				throw new IllegalStateException("pattern is zero string.");
			}
		}
		else if(this.patternCode <= 0)
		{
			result = false;
			if(throwException && !result)
			{
				throw new IllegalStateException("patternCode is zero.");
			}
		}
		else if(!this.contentUriInfo.isValid())
		{
			result = false;
			if(throwException && !result)
			{
				throw new IllegalStateException("contentUriInfo is invalid.");
			}
		}
		else if(!this.contentMimeTypeVndInfo.isValid())
		{
			result = false;
			if(throwException && !result)
			{
				throw new IllegalStateException("contentMimeTypeVndInfo is invalid.");
			}
		}
		else if(!this.mimeTypeVnd.isValid())
		{
			result = false;
			if(throwException && !result)
			{
				throw new IllegalStateException("mimeTypeVnd is invalid.");
			}
		}
		return result;
	}

	/**
	 * Do not call this method. This is only used MatcherController.
	 * @see com.tojc.ormlite.android.OrmLiteDefaultContentProvider.MatcherController#hasPreinitialized()
	 */
	public void setPreinitialized()
	{
		this.preinitialized = true;
	}
	
	/**
	 * Set the ContentUri. This is used when you are not using the DefaultContentUri annotation,
	 * or want to override the setting of the DefaultContentUri annotation.
	 * This method can not be called after MatcherController#hasPreinitialized().
	 * @see com.tojc.ormlite.android.annotation.AdditionalAnnotation.ContentUriInfo
	 * @see com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentUri
	 * @see com.tojc.ormlite.android.OrmLiteDefaultContentProvider.MatcherController#hasPreinitialized()
	 * @param contentUriInfo
	 * @return Instance of the MatcherPattern class.
	 */
	public MatcherPattern setContentUri(ContentUriInfo contentUriInfo)
	{
		if(this.preinitialized)
		{
			throw new IllegalStateException("Can't change the settings after initialization.");
		}
		this.contentUriInfo = contentUriInfo;
		return this;
	}

	/**
	 * Set the ContentUri. This is used when you are not using the DefaultContentUri annotation,
	 * or want to override the setting of the DefaultContentUri annotation.
	 * This method can not be called after MatcherController#hasPreinitialized().
	 * @see com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentUri
	 * @see com.tojc.ormlite.android.OrmLiteDefaultContentProvider.MatcherController#hasPreinitialized()
	 * @param authority
	 * @param path
	 * @return Instance of the MatcherPattern class.
	 */
	public MatcherPattern setContentUri(String authority, String path)
	{
		return this.setContentUri(new ContentUriInfo(authority, path));
	}
	
	/**
	 * Set the MIME types. This is used when you are not using the DefaultContentMimeTypeVnd annotation,
	 * or want to override the setting of the DefaultContentMimeTypeVnd annotation.
	 * This method can not be called after MatcherController#hasPreinitialized().
	 * @see com.tojc.ormlite.android.annotation.AdditionalAnnotation.ContentMimeTypeVndInfo
	 * @see com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentMimeTypeVnd
	 * @see com.tojc.ormlite.android.OrmLiteDefaultContentProvider.MatcherController#hasPreinitialized()
	 * @param contentMimeTypeVndInfo
	 * @return Instance of the MatcherPattern class.
	 */
	public MatcherPattern setContentMimeTypeVnd(ContentMimeTypeVndInfo contentMimeTypeVndInfo)
	{
		if(this.preinitialized)
		{
			throw new IllegalStateException("Can't change the settings after initialization.");
		}
		this.contentMimeTypeVndInfo = contentMimeTypeVndInfo;
		this.mimeTypeVnd = new MimeTypeVnd(this.subType, this.contentMimeTypeVndInfo);
		return this;
	}
	
	/**
	 * Set the MIME types. This is used when you are not using the DefaultContentMimeTypeVnd annotation,
	 * or want to override the setting of the DefaultContentMimeTypeVnd annotation.
	 * This method can not be called after MatcherController#hasPreinitialized().
	 * @see com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentMimeTypeVnd
	 * @see com.tojc.ormlite.android.OrmLiteDefaultContentProvider.MatcherController#hasPreinitialized()
	 * @param name
	 * @param type
	 * @return Instance of the MatcherPattern class.
	 */
	public MatcherPattern setContentMimeTypeVnd(String name, String type)
	{
		return this.setContentMimeTypeVnd(new ContentMimeTypeVndInfo(name, type));
	}


	public TableInfo getTableInfo()
	{
		return this.tableInfo;
	}

	public SubType getSubType()
	{
		return this.subType;
	}

	public String getPattern()
	{
		return this.pattern;
	}

	public int getPatternCode()
	{
		return this.patternCode;
	}


	public ContentUriInfo getContentUriInfo()
	{
		return this.contentUriInfo;
	}

//	public ContentMimeTypeVndInfo getContentMimeTypeVndInfo()
//	{
//		return this.contentMimeTypeVndInfo;
//	}

	public MimeTypeVnd getMimeTypeVnd()
	{
		return this.mimeTypeVnd;
	}

	/**
	 * @return Return the concatenation string of Path and Pattern from ContentUri.
	 *     <br>ex)<br>
	 *     <code>
	 *         ContentUri = "content://com.example.app.provider/table2/dataset1"<br>
	 *         Return = "table2/dataset1"<br>
	 *     </code>
	 */
	public String getPathAndPatternString()
	{
		return this.contentUriInfo.getPath() + "/" + this.pattern;
	}

	/**
	 * @return Returns the full ContentUri.(Uri object)
	 */
	public Uri getContentUriPattern()
	{
		return Uri.parse(this.contentUriInfo.getContentUri() + "/" + this.pattern);
	}
	
	/**
	 * @return Returns the full MIME types string.
	 *     <br>ex)<br>
	 *     <code>
	 *         Return = "vnd.android.cursor.item/vnd.com.example.provider.table1"<br>
	 *     </code>
	 */
	public String getMimeTypeVndString()
	{
		return this.mimeTypeVnd.getMimeTypeString();
	}

	@Override
	public String toString()
	{
		return getContentUriPattern().toString();
	}

}
