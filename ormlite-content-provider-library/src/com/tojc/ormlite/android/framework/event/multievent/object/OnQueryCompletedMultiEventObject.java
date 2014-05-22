package com.tojc.ormlite.android.framework.event.multievent.object;

import android.database.Cursor;
import android.net.Uri;

import com.tojc.ormlite.android.framework.MatcherPattern;
import com.tojc.ormlite.android.framework.OperationParameters;
import com.tojc.ormlite.android.framework.Parameter;
import com.tojc.ormlite.android.framework.event.multievent.MultiEventObjectBase;

/**
 * Created by Jaken on 2014/05/06.
 */
public class OnQueryCompletedMultiEventObject extends MultiEventObjectBase {
    private final Cursor result;
    private final Uri uri;
    private final MatcherPattern matcherPattern;
    private final Parameter parameter;

    public OnQueryCompletedMultiEventObject(Object source, Cursor result, Uri uri, MatcherPattern matcherPattern, Parameter parameter) {
        super(source);
        this.result = result;
        this.uri = uri;
        this.matcherPattern = matcherPattern;
        this.parameter = parameter;
    }

    public Cursor getResult() {
        return this.result;
    }

    public Uri getUri() {
        return this.uri;
    }

    public MatcherPattern getMatcherPattern() {
        return this.matcherPattern;
    }

    public OperationParameters.QueryParameters getParameter() {
        return this.parameter;
    }
}
