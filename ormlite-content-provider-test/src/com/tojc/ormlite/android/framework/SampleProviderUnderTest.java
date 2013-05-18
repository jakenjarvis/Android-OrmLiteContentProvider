package com.tojc.ormlite.android.framework;

import com.tojc.ormlite.android.OrmLiteSimpleContentProvider;
import com.tojc.ormlite.android.framework.MimeTypeVnd.SubType;
import com.tojc.ormlite.android.test.model.Account;
import com.tojc.ormlite.android.test.provider.AccountContract;
import com.tojc.ormlite.android.test.provider.SampleHelper;

public class SampleProviderUnderTest extends OrmLiteSimpleContentProvider<SampleHelper> {
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
