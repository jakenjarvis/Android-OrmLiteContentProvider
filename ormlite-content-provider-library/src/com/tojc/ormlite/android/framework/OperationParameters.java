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
package com.tojc.ormlite.android.framework;

import android.content.ContentValues;
import android.net.Uri;

/**
 * This keeps the parameters of Operation. Through the interface and exposes
 * only the methods required for event.
 * @author Jaken
 */
public class OperationParameters {
    public interface OperationParametersBaseInterface {
        Uri getUri();
    }

    public interface QueryParameters extends OperationParametersBaseInterface {
        String[] getProjection();

        String getSelection();

        String[] getSelectionArgs();

        String getSortOrder();
    }

    public interface InsertParameters extends OperationParametersBaseInterface {
        ContentValues getValues();
    }

    public interface DeleteParameters extends OperationParametersBaseInterface {
        String getSelection();

        String[] getSelectionArgs();
    }

    public interface UpdateParameters extends OperationParametersBaseInterface {
        ContentValues getValues();

        String getSelection();

        String[] getSelectionArgs();
    }
}
