package com.tojc.ormlite.android.ormlitecontentprovidersample.provider;

import com.tojc.ormlite.android.OrmLiteSimpleContentProvider;
import com.tojc.ormlite.android.framework.MimeTypeVnd.SubType;

public class MyProvider extends OrmLiteSimpleContentProvider<SampleHelper>
{
	@Override
	protected Class<SampleHelper> getHelperClass()
	{
		return SampleHelper.class;
	}

	@Override
	public boolean onCreate()
	{
		Controller = new MatcherController()
			.add(Account.class, SubType.Directory, "", Contract.Account.CONTENT_URI_PATTERN_MANY)
			.add(Account.class, SubType.Item, "#", Contract.Account.CONTENT_URI_PATTERN_ONE)
			.initialize();
		return true;
	}
}
