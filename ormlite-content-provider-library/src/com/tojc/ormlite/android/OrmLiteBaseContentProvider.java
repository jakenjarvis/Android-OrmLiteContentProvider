/*
 * This file is part of the Android-OrmLiteContentProvider package.
 *
 * Copyright (c) 2012, Android-OrmLiteContentProvider Team.
 *                     Jaken Jarvis (jaken.jarvis@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
 * Base class to use for ContentProvider in Android. Like OrmLiteBaseActivity, this class is a thin
 * wrapper.
 * @see com.j256.ormlite.android.apptools.OrmLiteBaseActivity
 * @author Jaken
 */
public abstract class OrmLiteBaseContentProvider<T extends OrmLiteSqliteOpenHelper> extends ContentProvider {
    private static Logger logger = LoggerFactory.getLogger(OrmLiteBaseContentProvider.class);

    private volatile T helper = null;
    private volatile boolean destroyed = false;

    protected abstract Class<T> getHelperClass();

    /**
     * Get a helper for this action. If you need to override, please consider createHelper().
     * @see com.tojc.ormlite.android.OrmLiteBaseContentProvider#createHelper()
     * @return Return an instance of the helper.
     */
    public T getHelper() {
        if (this.helper == null) {
            if (this.destroyed) {
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
    protected T createHelper() {
        return OpenHelperManager.getHelper(this.getContext(), this.getHelperClass());
    }

    /**
     * Release the Helper object. If you want to change function, please override this method.
     * @see com.tojc.ormlite.android.OrmLiteBaseContentProvider#createHelper()
     */
    protected void releaseHelper() {
        OpenHelperManager.releaseHelper();
    }

    /**
     * Get a connection source for this action.
     * @see com.j256.ormlite.support.ConnectionSource
     * @return Return an instance of the ConnectionSource.
     */
    public ConnectionSource getConnectionSource() {
        return getHelper().getConnectionSource();
    }

    @Override
    public void shutdown() {
        super.shutdown();
        if (this.helper != null) {
            this.helper.close();
            this.helper = null;
            this.releaseHelper();
            logger.trace("{}: helper {} was released, set to null", this, this.helper);
            this.destroyed = true;
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "@" + Integer.toHexString(super.hashCode());
    }
}
