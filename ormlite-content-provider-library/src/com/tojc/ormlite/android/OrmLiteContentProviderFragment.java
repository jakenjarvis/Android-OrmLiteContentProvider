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
import com.tojc.ormlite.android.framework.event.multievent.MultiEventListenerInterfaceBase;

import java.io.Serializable;

/**
 * This class is the base class for ContentProviderFragment. All fragments must inherit from this class.
 *
 * @author Jaken
 * @since 1.0.5
 */
public abstract class OrmLiteContentProviderFragment<U extends OrmLiteBaseContentProvider<T>, T extends OrmLiteSqliteOpenHelper> implements MultiEventListenerInterfaceBase, Serializable {
    private static final long serialVersionUID = 6174454914640278455L;

    private U contentProvider = null;

    public OrmLiteContentProviderFragment() {
        // This constructor is required.
    }

    /**
     * Please do not use this method. This argument, there is likely to change in the future.
     *
     * @param matcherController
     */
    @SuppressWarnings("unchecked")
    public final void onFragmentInitialize(MatcherController matcherController) {
        this.contentProvider = (U) matcherController.getContentProvider();
        this.onAppendMatcherPatterns(matcherController);
    }

    /**
     * Please return the implementation class of the fragment.
     *
     * @return
     */
    public abstract Class<? extends OrmLiteContentProviderFragment<U, T>> getFragmentClass();

    /**
     * Called at the time to add a MatcherPattern that are associated with this fragment.
     *
     * @param matcherController
     */
    protected abstract void onAppendMatcherPatterns(MatcherController matcherController);

    /**
     * This method will return the event handling type of this fragment.
     * If you want to change the behavior of the event call, please override this method.
     *
     * @return
     * @see com.tojc.ormlite.android.framework.event.FragmentEventHandling
     */
    public int getFragmentEventHandling() {
        return FragmentEventHandling.FRAGMENT_ONLY;
    }

    /**
     * This method will return the events key of fragment.
     * It is good if any unique string. It is necessary to return the same value at all times.
     * If you want to change, please override this method.
     *
     * @return events key string
     */
    public String getKeyName() {
        return this.getFragmentClass().getName();
    }

    /**
     * Get a ContentProvider for this action.
     *
     * @return
     * @see android.content.ContentProvider
     * @see com.tojc.ormlite.android.OrmLiteBaseContentProvider
     * @see com.tojc.ormlite.android.OrmLiteClassifierContentProvider
     */
    public U getContentProvider() {
        return this.contentProvider;
    }

    /**
     * Get a helper for this action.
     *
     * @return Return an instance of the helper.
     * @see com.tojc.ormlite.android.OrmLiteBaseContentProvider#getHelper()
     */
    public T getHelper() {
        return this.getContentProvider().getHelper();
    }

    /**
     * Get a connection source for this action.
     *
     * @return
     * @see com.j256.ormlite.support.ConnectionSource
     * @see com.tojc.ormlite.android.OrmLiteBaseContentProvider#getConnectionSource()
     */
    public ConnectionSource getConnectionSource() {
        return this.getHelper().getConnectionSource();
    }
}
