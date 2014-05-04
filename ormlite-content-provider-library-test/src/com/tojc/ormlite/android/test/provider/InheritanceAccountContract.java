package com.tojc.ormlite.android.test.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Jaken on 2014/05/04.
 */
public final class InheritanceAccountContract implements BaseColumns {

    private InheritanceAccountContract() {
        // utility constructor
    }

    public static final String AUTHORITY = "com.tojc.ormlite.android.test";

    public static final String CONTENT_URI_PATH = "inheritance_accounts";

    public static final String MIMETYPE_TYPE = "inheritance_accounts";
    public static final String MIMETYPE_NAME = "com.tojc.ormlite.android.test.provider";

    public static final Uri CONTENT_URI = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(AUTHORITY).appendPath(CONTENT_URI_PATH).build();

    public static final String NAME = "name";
    public static final String ADDRESS = "address";
}
