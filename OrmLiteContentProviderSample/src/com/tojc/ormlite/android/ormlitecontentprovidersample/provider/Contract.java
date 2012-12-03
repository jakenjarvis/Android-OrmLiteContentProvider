package com.tojc.ormlite.android.ormlitecontentprovidersample.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class Contract
{
	public static final String DATABASE_NAME = "MyDatabase";
	public static final int DATABASE_VERSION = 1;

	public static final String AUTHORITY = "com.tojc.ormlite.android.ormlitecontentprovidersample";

    // accounts table info
	public static class Account implements BaseColumns
	{
		public static final String TABLENAME = "accounts";

		public static final String CONTENT_URI_PATH = TABLENAME;

		public static final String MIMETYPE_TYPE = TABLENAME;
		public static final String MIMETYPE_NAME = AUTHORITY + ".provider";
		
		// feild info
		public static final String NAME = "name";
		
		// content uri pattern code
		public static final int CONTENT_URI_PATTERN_MANY = 1;
		public static final int CONTENT_URI_PATTERN_ONE = 2;

		// Refer to activity.
		public static final Uri contentUri = new Uri.Builder()
	        .scheme(ContentResolver.SCHEME_CONTENT)
	        .authority(AUTHORITY)
	        .appendPath(CONTENT_URI_PATH)
	        .build();
	}
}
