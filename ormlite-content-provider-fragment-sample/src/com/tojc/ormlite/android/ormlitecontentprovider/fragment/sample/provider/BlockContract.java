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
package com.tojc.ormlite.android.ormlitecontentprovider.fragment.sample.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class BlockContract implements BaseColumns {

    public static final String TABLE_NAME = "Block";
    public static final String AUTHORITY = "com.tojc.ormlite.android.ormlitecontentprovider.fragment.sample";
    public static final String CONTENT_URI_PATH = "blocks";
    public static final String MIMETYPE_TYPE = "blocks";
    public static final String MIMETYPE_NAME = "com.tojc.ormlite.android.ormlitecontentprovider.fragment.sample.provider";

    public static final Uri CONTENT_URI = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(AUTHORITY).appendPath(CONTENT_URI_PATH).build();

    // Column name
    public static final String NAME = "name";

    // Blocks "blocks/#"
    public static final String PATTERN_BLOCKS_ID = "#";
    public static final int CONTENT_URI_PATTERN_BLOCKS_ID = 7;
    public static Uri buildBlockUri(int blockId) {
        return CONTENT_URI.buildUpon().appendPath(String.valueOf(blockId)).build();
    }

    // Blocks Name "blocks/name/*"
    public static final String PATTERN_BLOCKS_NAME = "name/*";
    public static final int CONTENT_URI_PATTERN_BLOCKS_NAME = 8;
    public static final String PATH_NAME = "name";
    public static Uri buildBlockNameUri(String blockName) {
        return CONTENT_URI.buildUpon().appendPath(PATH_NAME).appendPath(blockName).build();
    }

    private BlockContract() {
        // Utility constructor
    }
}
