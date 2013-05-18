package com.tojc.ormlite.android.framework;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.tojc.ormlite.android.annotation.info.ContentMimeTypeVndInfo;
import com.tojc.ormlite.android.framework.MimeTypeVnd.SubType;

@SmallTest
public class MimeTypeVndTest extends AndroidTestCase {

    private static final String CONTENT_MIME_TYPE_VND_NAME = "bar";
    private static final String CONTENT_MIME_TYPE_VND_TYPE = "foo";

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
        mimeTypeVnd = new MimeTypeVnd(SubType.Item, new ContentMimeTypeVndInfo(null, CONTENT_MIME_TYPE_VND_TYPE));
        // when

        // then
        assertFalse(mimeTypeVnd.isValid());

        // --
        // given
        mimeTypeVnd = new MimeTypeVnd(SubType.Item, new ContentMimeTypeVndInfo(StringUtils.EMPTY, CONTENT_MIME_TYPE_VND_TYPE));
        // when

        // then
        assertFalse(mimeTypeVnd.isValid());
    }

    public void testIsValid_returns_false_if_mime_type_has_null_or_empty_type() {
        // given
        mimeTypeVnd = new MimeTypeVnd(SubType.Item, new ContentMimeTypeVndInfo(CONTENT_MIME_TYPE_VND_NAME, null));
        // when

        // then
        assertFalse(mimeTypeVnd.isValid());

        // --
        // given
        mimeTypeVnd = new MimeTypeVnd(SubType.Item, new ContentMimeTypeVndInfo(CONTENT_MIME_TYPE_VND_NAME, StringUtils.EMPTY));
        // when

        // then
        assertFalse(mimeTypeVnd.isValid());
    }

    public void testGetMimeTypeString() {
        mimeTypeVnd = new MimeTypeVnd(SubType.Item, new ContentMimeTypeVndInfo(CONTENT_MIME_TYPE_VND_NAME, CONTENT_MIME_TYPE_VND_TYPE));

        final String expectedTypeString = SubType.Item.toString() + File.separator + ContentMimeTypeVndInfo.VND + ContentMimeTypeVndInfo.VND_SEPARATOR + CONTENT_MIME_TYPE_VND_NAME
                + ContentMimeTypeVndInfo.VND_SEPARATOR + CONTENT_MIME_TYPE_VND_TYPE;

        assertEquals(expectedTypeString, mimeTypeVnd.getMimeTypeString());
    }

}
