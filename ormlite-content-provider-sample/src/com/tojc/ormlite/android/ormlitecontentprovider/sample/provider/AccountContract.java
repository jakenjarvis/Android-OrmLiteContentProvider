package com.tojc.ormlite.android.ormlitecontentprovider.sample.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class AccountContract implements BaseColumns {
    public static final String AUTHORITY = "com.tojc.ormlite.android.ormlitecontentprovider.sample";

    public static final String CONTENT_URI_PATH = "accounts";

    public static final String MIMETYPE_TYPE = "accounts";
    public static final String MIMETYPE_NAME = "com.tojc.ormlite.android.ormlitecontentprovider.sample.provider";

    public static final int CONTENT_URI_PATTERN_MANY = 1;
    public static final int CONTENT_URI_PATTERN_ONE = 2;

    public static final Uri CONTENT_URI = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(AUTHORITY).appendPath(CONTENT_URI_PATH).build();

    public static final String NAME = "name";
}
