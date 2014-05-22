package com.tojc.ormlite.android.event.listener;

import android.net.Uri;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.tojc.ormlite.android.framework.MatcherPattern;
import com.tojc.ormlite.android.framework.OperationParameters.UpdateParameters;
import com.tojc.ormlite.android.framework.event.expandevent.ContentProviderEventListenerInterfaceBase;

/**
 * Created by Jaken on 2014/05/05.
 */
public interface OnUpdateCompletedListener<T extends OrmLiteSqliteOpenHelper> extends ContentProviderEventListenerInterfaceBase {
    /**
     * This method is called after the onUpdate processing has been handled. If you're a need,
     * you can override this method.
     *
     * @param result    This is the return value of onUpdate method.
     * @param uri       This is the Uri of target.
     * @param target    This is identical to the argument of onUpdate method.
     *                  It is MatcherPattern objects that match to evaluate Uri by UriMatcher. You can
     *                  access information in the tables and columns, ContentUri, MimeType etc.
     * @param parameter This is identical to the argument of onUpdate method.
     *                  Arguments passed to the update() method.
     * @since 1.0.4
     */
    void onUpdateCompleted(int result, Uri uri, MatcherPattern target, UpdateParameters parameter);
}
