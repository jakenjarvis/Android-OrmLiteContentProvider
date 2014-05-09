package com.tojc.ormlite.android.framework.event.multieventobject;

import android.net.Uri;

import com.tojc.ormlite.android.framework.event.multieventbase.MultiEventObjectBase;

/**
 * Created by Jaken on 2014/05/06.
 */
public class OnBulkInsertCompletedMultiEventObject extends MultiEventObjectBase {
    private final int result;
    private final Uri uri;

    public OnBulkInsertCompletedMultiEventObject(Object source, int result, Uri uri) {
        super(source);
        this.result = result;
        this.uri = uri;
    }

    public int getResult() {
        return this.result;
    }

    public Uri getUri() {
        return this.uri;
    }
}
