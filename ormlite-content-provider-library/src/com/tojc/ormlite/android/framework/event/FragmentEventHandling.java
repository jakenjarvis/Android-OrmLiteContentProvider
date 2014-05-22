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
package com.tojc.ormlite.android.framework.event;

/**
 * Created by Jaken on 2014/05/12.
 */
public final class FragmentEventHandling {
    /**
     * Events notify the only fragment. If listener is not implemented, it does nothing.
     */
    public static final int FRAGMENT_ONLY = 1;
    /**
     * If listener of fragment is not implemented, forward to default.
     * This is useful for implementing a fragment only some processing.
     */
    public static final int FRAGMENT_AND_DEFAULT_FORWARD = 2;
    /**
     * Events notify the content provider along with the fragment.
     * You must be careful that the event is duplicated.
     */
    public static final int FRAGMENT_AND_DEFAULT_DUPLICATE = 3;

    private FragmentEventHandling() {
    }
}
