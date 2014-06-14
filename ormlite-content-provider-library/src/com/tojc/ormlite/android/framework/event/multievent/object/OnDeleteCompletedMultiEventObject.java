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
package com.tojc.ormlite.android.framework.event.multievent.object;

import android.net.Uri;

import com.tojc.ormlite.android.framework.MatcherPattern;
import com.tojc.ormlite.android.framework.OperationParameters;
import com.tojc.ormlite.android.framework.Parameter;
import com.tojc.ormlite.android.framework.event.multievent.MultiEventObjectBase;

/**
 * Created by Jaken on 2014/05/06.
 */
public class OnDeleteCompletedMultiEventObject extends MultiEventObjectBase {
    private final int result;
    private final Uri uri;
    private final MatcherPattern matcherPattern;
    private final Parameter parameter;

    public OnDeleteCompletedMultiEventObject(Object source, int result, Uri uri, MatcherPattern matcherPattern, Parameter parameter) {
        super(source);
        this.result = result;
        this.uri = uri;
        this.matcherPattern = matcherPattern;
        this.parameter = parameter;
    }

    public int getResult() {
        return this.result;
    }

    public Uri getUri() {
        return this.uri;
    }

    public MatcherPattern getMatcherPattern() {
        return this.matcherPattern;
    }

    public OperationParameters.DeleteParameters getParameter() {
        return this.parameter;
    }
}
