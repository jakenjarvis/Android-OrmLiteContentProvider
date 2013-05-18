package com.tojc.ormlite.android.annotation.info;

import org.apache.commons.lang3.StringUtils;

import android.test.AndroidTestCase;

import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentMimeTypeVnd;

public class ContentMimeTypeVndInfoTest extends AndroidTestCase {

    private static final String TEST_NAME = "foo";
    private static final String TEST_TYPE = "bar";

    private ContentMimeTypeVndInfo contentMimeTypeVndInfo;

    public void testIsValidValue_returns_false_for_null_or_empty_package_or_class() {
        // given
        contentMimeTypeVndInfo = new ContentMimeTypeVndInfo(null, null);
        // when

        // then
        assertFalse(contentMimeTypeVndInfo.isValidValue());

        // --
        // given
        contentMimeTypeVndInfo = new ContentMimeTypeVndInfo(null, TEST_TYPE);
        // when

        // then
        assertFalse(contentMimeTypeVndInfo.isValidValue());

        // --
        // given
        contentMimeTypeVndInfo = new ContentMimeTypeVndInfo(TEST_NAME, null);
        // when

        // then
        assertFalse(contentMimeTypeVndInfo.isValidValue());

        // --
        // given
        contentMimeTypeVndInfo = new ContentMimeTypeVndInfo(TEST_NAME, StringUtils.EMPTY);
        // when

        // then
        assertFalse(contentMimeTypeVndInfo.isValidValue());

        // --
        // given
        contentMimeTypeVndInfo = new ContentMimeTypeVndInfo(StringUtils.EMPTY, TEST_TYPE);
        // when

        // then
        assertFalse(contentMimeTypeVndInfo.isValidValue());
    }

    public void testIsValidValue_returns_true() {
        // given
        contentMimeTypeVndInfo = new ContentMimeTypeVndInfo(TEST_NAME, TEST_TYPE);
        // when

        // then
        assertTrue(contentMimeTypeVndInfo.isValidValue());
    }

    public void testGetName() {
        // given
        contentMimeTypeVndInfo = new ContentMimeTypeVndInfo(TEST_NAME, TEST_TYPE);
        // when

        // then
        assertEquals(TEST_NAME, contentMimeTypeVndInfo.getName());
    }

    public void testGetType() {
        // given
        contentMimeTypeVndInfo = new ContentMimeTypeVndInfo(TEST_NAME, TEST_TYPE);
        // when

        // then
        assertEquals(TEST_TYPE, contentMimeTypeVndInfo.getType());
    }

    public void testGetVndProviderSpecificString() {
        // given
        contentMimeTypeVndInfo = new ContentMimeTypeVndInfo(TEST_NAME, TEST_TYPE);
        // when

        // then
        assertEquals(ContentMimeTypeVndInfo.VND + "." + TEST_NAME + "." + TEST_TYPE, contentMimeTypeVndInfo.getVndProviderSpecificString());
    }

    public void testIsValidValue_returns_right_values_for_annotated_element_without_params() {
        // given
        contentMimeTypeVndInfo = new ContentMimeTypeVndInfo(AnnotatedClassUnderTestNoParams.class);
        // when

        // then
        assertEquals(AnnotatedClassUnderTestNoParams.class.getPackage().getName() + ContentMimeTypeVndInfo.PROVIDER_SUFFIX, contentMimeTypeVndInfo.getName());
        assertEquals(AnnotatedClassUnderTestNoParams.class.getSimpleName().toLowerCase(), contentMimeTypeVndInfo.getType());
        assertTrue(contentMimeTypeVndInfo.isValidValue());
    }

    public void testIsValidValue_returns_right_values_for_annotated_element_with_params() {
        // given
        contentMimeTypeVndInfo = new ContentMimeTypeVndInfo(AnnotatedClassUnderTestWithParams.class);
        // when

        // then
        assertEquals(TEST_NAME, contentMimeTypeVndInfo.getName());
        assertEquals(TEST_TYPE, contentMimeTypeVndInfo.getType());
        assertTrue(contentMimeTypeVndInfo.isValidValue());
    }

    public void testIsValidValue_returns_right_values_for_non_annotated_element() {
        // given
        contentMimeTypeVndInfo = new ContentMimeTypeVndInfo(NonAnnotatedClassUnderTest.class);
        // when

        // then
        assertNull(contentMimeTypeVndInfo.getName());
        assertNull(contentMimeTypeVndInfo.getType());
        assertFalse(contentMimeTypeVndInfo.isValidValue());
    }

    /**
     * Annotated class under test.
     */
    @DefaultContentMimeTypeVnd
    private class AnnotatedClassUnderTestNoParams {
    }

    /**
     * Annotated class under test.
     */
    @DefaultContentMimeTypeVnd(name = TEST_NAME, type = TEST_TYPE)
    private class AnnotatedClassUnderTestWithParams {
    }

    /**
     * Non-Annotated class under test.
     */
    private class NonAnnotatedClassUnderTest {
    }

}
