package com.tojc.ormlite.android.event.listener;

import android.database.Cursor;
import android.net.Uri;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.tojc.ormlite.android.framework.MatcherPattern;
import com.tojc.ormlite.android.framework.OperationParameters.QueryParameters;
import com.tojc.ormlite.android.framework.event.expandevent.ContentProviderEventListenerInterfaceBase;

/**
 * Created by Jaken on 2014/05/05.
 */
public interface OnQueryCompletedListener<T extends OrmLiteSqliteOpenHelper> extends ContentProviderEventListenerInterfaceBase {
    /**
     * This method is called after the onQuery processing has been handled. If you're a need,
     * you can override this method.
     *
     * @param result    This is the return value of onQuery method.
     * @param uri       This is the Uri of target.
     * @param target    This is identical to the argument of onQuery method.
     *                  It is MatcherPattern objects that match to evaluate Uri by UriMatcher. You can
     *                  access information in the tables and columns, ContentUri, MimeType etc.
     * @param parameter This is identical to the argument of onQuery method.
     *                  Arguments passed to the query() method.
     * @since 1.0.4
     */
    void onQueryCompleted(Cursor result, Uri uri, MatcherPattern target, QueryParameters parameter);
}
