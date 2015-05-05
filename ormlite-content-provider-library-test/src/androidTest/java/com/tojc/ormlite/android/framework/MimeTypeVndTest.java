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

import java.io.File;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.tojc.ormlite.android.annotation.info.ContentMimeTypeVndInfo;
import com.tojc.ormlite.android.framework.MimeTypeVnd.SubType;

@SmallTest
public class MimeTypeVndTest extends AndroidTestCase {

    private static final String CONTENT_MIME_TYPE_VND_NAME = "bar";
    private static final String CONTENT_MIME_TYPE_VND_TYPE = "foo";
    private static final String EMPTY = "";

    private MimeTypeVnd mimeTypeVnd;

    public void testIsValid_returns_false_if_subtype_is_null() {
        // given
        mimeTypeVnd = new MimeTypeVnd(null, new ContentMimeTypeVndInfo(CONTENT_MIME_TYPE_VND_NAME, CONTENT_MIME_TYPE_VND_TYPE));
        // when

        // then
        assertFalse(mimeTypeVnd.isValid());
    }

    public void testIsValid_returns_false_if_mime_type_has_null_or_empty_path() {
        // given
        mimeTypeVnd = new MimeTypeVnd(SubType.ITEM, new ContentMimeTypeVndInfo(null, CONTENT_MIME_TYPE_VND_TYPE));
        // when

        // then
        assertFalse(mimeTypeVnd.isValid());

        // --
        // given
        mimeTypeVnd = new MimeTypeVnd(SubType.ITEM, new ContentMimeTypeVndInfo(EMPTY, CONTENT_MIME_TYPE_VND_TYPE));
        // when

        // then
        assertFalse(mimeTypeVnd.isValid());
    }

    public void testIsValid_returns_false_if_mime_type_has_null_or_empty_type() {
        // given
        mimeTypeVnd = new MimeTypeVnd(SubType.ITEM, new ContentMimeTypeVndInfo(CONTENT_MIME_TYPE_VND_NAME, null));
        // when

        // then
        assertFalse(mimeTypeVnd.isValid());

        // --
        // given
        mimeTypeVnd = new MimeTypeVnd(SubType.ITEM, new ContentMimeTypeVndInfo(CONTENT_MIME_TYPE_VND_NAME, EMPTY));
        // when

        // then
        assertFalse(mimeTypeVnd.isValid());
    }

    public void testGetMimeTypeString() {
        mimeTypeVnd = new MimeTypeVnd(SubType.ITEM, new ContentMimeTypeVndInfo(CONTENT_MIME_TYPE_VND_NAME, CONTENT_MIME_TYPE_VND_TYPE));

        final String expectedTypeString = SubType.ITEM.toString() + File.separator + ContentMimeTypeVndInfo.VND + ContentMimeTypeVndInfo.VND_SEPARATOR + CONTENT_MIME_TYPE_VND_NAME
                + ContentMimeTypeVndInfo.VND_SEPARATOR + CONTENT_MIME_TYPE_VND_TYPE;

        assertEquals(expectedTypeString, mimeTypeVnd.getMimeTypeString());
    }

}
