package com.tojc.ormlite.android.test.provider;

import com.tojc.ormlite.android.OrmLiteSimpleContentProvider;
import com.tojc.ormlite.android.framework.MatcherController;
import com.tojc.ormlite.android.framework.MimeTypeVnd.SubType;
import com.tojc.ormlite.android.test.model.Account;
import com.tojc.ormlite.android.test.model.Membership;

public class UnderTestSampleProvider extends OrmLiteSimpleContentProvider<SampleHelper> {
    @Override
    protected Class<SampleHelper> getHelperClass() {
        return SampleHelper.class;
    }

    @Override
    public boolean onCreate() {
        int patternCode = 1;
        setMatcherController(new MatcherController()//
                .add(Account.class, SubType.DIRECTORY, "", patternCode++)//
                .add(Account.class, SubType.ITEM, "#", patternCode++)//
                .add(Membership.class, SubType.DIRECTORY, "", patternCode++)//
                .add(Membership.class, SubType.ITEM, "#", patternCode++));
        return true;
    }
}
