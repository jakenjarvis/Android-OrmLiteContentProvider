package com.tojc.ormlite.android.ormlitecontentprovider.sample.provider;

import com.tojc.ormlite.android.OrmLiteSimpleContentProvider;
import com.tojc.ormlite.android.framework.MatcherController;
import com.tojc.ormlite.android.framework.MimeTypeVnd.SubType;
import com.tojc.ormlite.android.ormlitecontentprovider.sample.model.Account;

public class SampleProvider extends OrmLiteSimpleContentProvider<SampleHelper> {
    @Override
    protected Class<SampleHelper> getHelperClass() {
        return SampleHelper.class;
    }

    @Override
    public boolean onCreate() {
        setMatcherController(new MatcherController()//
                .add(Account.class, SubType.DIRECTORY, "", AccountContract.CONTENT_URI_PATTERN_MANY)//
                .add(Account.class, SubType.ITEM, "#", AccountContract.CONTENT_URI_PATTERN_ONE));
        return true;
    }
}
