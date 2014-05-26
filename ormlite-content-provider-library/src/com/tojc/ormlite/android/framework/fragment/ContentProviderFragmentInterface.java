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
package com.tojc.ormlite.android.framework.fragment;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.tojc.ormlite.android.OrmLiteBaseContentProvider;
import com.tojc.ormlite.android.framework.MatcherController;
import com.tojc.ormlite.android.framework.event.multievent.MultiEventListenerInterfaceBase;

import java.io.Serializable;

/**
 * Created by Jaken on 2014/05/26.
 * <p/>
 * NOTE: This interface has the potential to change the interface in the future.
 *
 * @since 1.0.5
 */
public interface ContentProviderFragmentInterface<U extends OrmLiteBaseContentProvider<T>, T extends OrmLiteSqliteOpenHelper> extends MultiEventListenerInterfaceBase, Serializable {
    /**
     * This method is called in timing to initialize the ContentProviderFragment.
     * This method is for internal use only. Please do not use this method.
     * <p/>
     * NOTE: This method has the potential to change the interface in the future.
     *
     * @param matcherController
     * @since 1.0.5
     */
    void onFragmentInitialize(MatcherController matcherController);

    /**
     * Please return the implementation class of the fragment.
     *
     * @return
     */
    Class<? extends ContentProviderFragmentInterface<U, T>> getFragmentClass();

    /**
     * This method will return the event handling type of this fragment.
     * If you want to change the behavior of the event call, please override this method.
     *
     * @return
     * @see com.tojc.ormlite.android.framework.event.FragmentEventHandling
     */
    int getFragmentEventHandling();

    /**
     * This method will return the events key of fragment.
     * It is good if any unique string. It is necessary to return the same value at all times.
     * If you want to change, please override this method.
     *
     * @return events key string
     */
    String getKeyName();

    /**
     * Get a ContentProvider for this action.
     *
     * @return
     * @see android.content.ContentProvider
     * @see com.tojc.ormlite.android.OrmLiteBaseContentProvider
     * @see com.tojc.ormlite.android.OrmLiteClassifierContentProvider
     */
    U getContentProvider();

    /**
     * Get a helper for this action.
     *
     * @return Return an instance of the helper.
     * @see com.tojc.ormlite.android.OrmLiteBaseContentProvider#getHelper()
     */
    T getHelper();

    /**
     * Get a connection source for this action.
     *
     * @return
     * @see com.j256.ormlite.support.ConnectionSource
     * @see com.tojc.ormlite.android.OrmLiteBaseContentProvider#getConnectionSource()
     */
    ConnectionSource getConnectionSource();
}
