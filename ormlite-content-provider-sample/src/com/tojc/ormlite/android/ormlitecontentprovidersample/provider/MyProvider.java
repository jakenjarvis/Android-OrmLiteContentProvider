package com.tojc.ormlite.android.ormlitecontentprovidersample.provider;

import com.tojc.ormlite.android.OrmLiteSimpleContentProvider;
import com.tojc.ormlite.android.framework.MimeTypeVnd.SubType;

public class MyProvider extends OrmLiteSimpleContentProvider<SampleHelper> {
    @Override
    protected Class<SampleHelper> getHelperClass() {
        return SampleHelper.class;
    }

    @Override
    public boolean onCreate() {
        setMatcherController(new MatcherController()//
                .add(Account.class, SubType.Directory, "", AccountContract.CONTENT_URI_PATTERN_MANY)//
                .add(Account.class, SubType.Item, "#", AccountContract.CONTENT_URI_PATTERN_ONE));
        return true;
    }
}
