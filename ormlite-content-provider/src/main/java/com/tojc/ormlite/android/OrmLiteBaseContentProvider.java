/*
 * This file is part of the Android-OrmLiteContentProvider package.
 * 
 * Copyright (c) 2012, Jaken Jarvis (jaken.jarvis@gmail.com)
 * 
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 * 
 * The author may be contacted via 
 * https://github.com/jakenjarvis/Android-OrmLiteContentProvider
 */
package com.tojc.ormlite.android;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.support.ConnectionSource;

import android.content.ContentProvider;

/**
 * Base class to use for ContentProvider in Android.
 * 
 * Like OrmLiteBaseActivity, this class is a thin wrapper.
 * @see com.j256.ormlite.android.apptools.OrmLiteBaseActivity
 * @author Jaken
 */
public abstract class OrmLiteBaseContentProvider<T extends OrmLiteSqliteOpenHelper> extends ContentProvider
{
	private static Logger logger = LoggerFactory.getLogger(OrmLiteBaseContentProvider.class);

	private volatile T helper = null;
	private volatile boolean destroyed = false;

	protected abstract Class<T> getHelperClass();

	/**
	 * Get a helper for this action. If you need to override, please consider createHelper().
	 * @see com.tojc.ormlite.android.OrmLiteBaseContentProvider#createHelper()
	 * @return Return an instance of the helper.
	 */
	public T getHelper()
	{
		if(this.helper == null)
		{
			if(this.destroyed)
			{
				throw new IllegalStateException("A call to shutdown has already been made and the helper cannot be used after that point");
			}
			this.helper = this.createHelper();
			logger.trace("{}: got new helper {} from OpenHelperManager", this, this.helper);
		}
		return this.helper;
	}

	/**
	 * Create the Helper object. If you want to change function, please override this method.
	 * @see com.tojc.ormlite.android.OrmLiteBaseContentProvider#releaseHelper()
	 * @return Return an instance of the helper.
	 */
	protected T createHelper()
	{
		return OpenHelperManager.getHelper(this.getContext(), this.getHelperClass());
	}

	/**
	 * Release the Helper object. If you want to change function, please override this method.
	 * @see com.tojc.ormlite.android.OrmLiteBaseContentProvider#createHelper()
	 */
	protected void releaseHelper()
	{
		OpenHelperManager.releaseHelper();
	}

	/**
	 * Get a connection source for this action.
	 * @see com.j256.ormlite.support.ConnectionSource
	 * @return Return an instance of the ConnectionSource.
	 */
	public ConnectionSource getConnectionSource()
	{
		return getHelper().getConnectionSource();
	}

	@Override
	public void shutdown()
	{
		super.shutdown();
		if(this.helper != null)
		{
			this.helper.close();
			this.helper = null;
			this.releaseHelper();
			logger.trace("{}: helper {} was released, set to null", this, this.helper);
			this.destroyed = true;
		}
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "@" + Integer.toHexString(super.hashCode());
	}
}
