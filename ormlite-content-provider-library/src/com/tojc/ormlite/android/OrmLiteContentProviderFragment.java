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

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.tojc.ormlite.android.framework.MatcherController;
import com.tojc.ormlite.android.framework.event.FragmentEventHandling;
import com.tojc.ormlite.android.framework.fragment.ContentProviderFragmentInterface;

/**
 * This class is the base class for ContentProviderFragment. All fragments must inherit from this class.
 *
 * @author Jaken
 * @since 2.0.0
 */
public abstract class OrmLiteContentProviderFragment<U extends OrmLiteBaseContentProvider<T>, T extends OrmLiteSqliteOpenHelper> implements ContentProviderFragmentInterface<U, T> {
    private static final long serialVersionUID = 6174454914640278455L;

    private U contentProvider = null;

    public OrmLiteContentProviderFragment() {
        // This constructor is required.
    }

    /**
     * @see com.tojc.ormlite.android.framework.fragment.ContentProviderFragmentInterface#onFragmentInitialize(com.tojc.ormlite.android.framework.MatcherController)
     */
    @Override
    @SuppressWarnings("unchecked")
    public final void onFragmentInitialize(MatcherController matcherController) {
        this.contentProvider = (U) matcherController.getContentProvider();
        this.onAppendMatcherPatterns(matcherController);
    }

    /**
     * @see com.tojc.ormlite.android.framework.fragment.ContentProviderFragmentInterface#getFragmentClass()
     */
    @Override
    public abstract Class<? extends OrmLiteContentProviderFragment<U, T>> getFragmentClass();

    /**
     * Called at the time to add a MatcherPattern that are associated with this fragment.
     *
     * @param matcherController
     */
    protected abstract void onAppendMatcherPatterns(MatcherController matcherController);

    /**
     * @see com.tojc.ormlite.android.framework.fragment.ContentProviderFragmentInterface#getFragmentEventHandling()
     */
    @Override
    public int getFragmentEventHandling() {
        return FragmentEventHandling.FRAGMENT_ONLY;
    }

    /**
     * @see com.tojc.ormlite.android.framework.fragment.ContentProviderFragmentInterface#getKeyName()
     */
    @Override
    public String getKeyName() {
        return this.getFragmentClass().getName();
    }

    /**
     * @see com.tojc.ormlite.android.framework.fragment.ContentProviderFragmentInterface#getContentProvider()
     */
    @Override
    public U getContentProvider() {
        return this.contentProvider;
    }

    /**
     * @see com.tojc.ormlite.android.framework.fragment.ContentProviderFragmentInterface#getHelper()
     */
    @Override
    public T getHelper() {
        return this.getContentProvider().getHelper();
    }

    /**
     * @see com.tojc.ormlite.android.framework.fragment.ContentProviderFragmentInterface#getConnectionSource()
     */
    @Override
    public ConnectionSource getConnectionSource() {
        return this.getHelper().getConnectionSource();
    }
}
