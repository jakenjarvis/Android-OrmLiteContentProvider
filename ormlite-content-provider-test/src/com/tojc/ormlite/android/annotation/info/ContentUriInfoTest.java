package com.tojc.ormlite.android.annotation.info;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentUri;

@SmallTest
public class ContentUriInfoTest extends AndroidTestCase {

    private static final String TEST_AUTHORITY = "foo";
    private static final String TEST_PATH = "bar";

    private ContentUriInfo ContentUriInfo;

    public void testIsValidValue_returns_false_for_null_or_empty_package_or_class() {
        // given
        ContentUriInfo = new ContentUriInfo(null, null);
        // when

        // then
        assertFalse(ContentUriInfo.isValidValue());

        // --
        // given
        ContentUriInfo = new ContentUriInfo(null, TEST_PATH);
        // when

        // then
        assertFalse(ContentUriInfo.isValidValue());

        // --
        // given
        ContentUriInfo = new ContentUriInfo(TEST_AUTHORITY, null);
        // when

        // then
        assertFalse(ContentUriInfo.isValidValue());

        // --
        // given
        ContentUriInfo = new ContentUriInfo(TEST_AUTHORITY, StringUtils.EMPTY);
        // when

        // then
        assertFalse(ContentUriInfo.isValidValue());

        // --
        // given
        ContentUriInfo = new ContentUriInfo(StringUtils.EMPTY, TEST_PATH);
        // when

        // then
        assertFalse(ContentUriInfo.isValidValue());
    }

    public void testIsValidValue_returns_true() {
        // given
        ContentUriInfo = new ContentUriInfo(TEST_AUTHORITY, TEST_PATH);
        // when

        // then
        assertTrue(ContentUriInfo.isValidValue());
    }

    public void testgetAuthority() {
        // given
        ContentUriInfo = new ContentUriInfo(TEST_AUTHORITY, TEST_PATH);
        // when

        // then
        assertEquals(TEST_AUTHORITY, ContentUriInfo.getAuthority());
    }

    public void testgetPath() {
        // given
        ContentUriInfo = new ContentUriInfo(TEST_AUTHORITY, TEST_PATH);
        // when

        // then
        assertEquals(TEST_PATH, ContentUriInfo.getPath());
    }

    public void testIsValidValue_returns_right_values_for_annotated_element_without_params() {
        // given
        ContentUriInfo = new ContentUriInfo(AnnotatedClassUnderTestNoParams.class);
        // when

        // then
        assertEquals(AnnotatedClassUnderTestNoParams.class.getPackage().getName(), ContentUriInfo.getAuthority());
        assertEquals(AnnotatedClassUnderTestNoParams.class.getSimpleName().toLowerCase(Locale.ENGLISH), ContentUriInfo.getPath());
        assertTrue(ContentUriInfo.isValidValue());
    }

    public void testIsValidValue_returns_right_values_for_annotated_element_with_params() {
        // given
        ContentUriInfo = new ContentUriInfo(AnnotatedClassUnderTestWithParams.class);
        // when

        // then
        assertEquals(TEST_AUTHORITY, ContentUriInfo.getAuthority());
        assertEquals(TEST_PATH, ContentUriInfo.getPath());
        assertTrue(ContentUriInfo.isValidValue());
    }

    public void testIsValidValue_returns_right_values_for_non_annotated_element() {
        // given
        ContentUriInfo = new ContentUriInfo(NonAnnotatedClassUnderTest.class);
        // when

        // then
        assertEquals(NonAnnotatedClassUnderTest.class.getPackage().getName(), ContentUriInfo.getAuthority());
        assertEquals(NonAnnotatedClassUnderTest.class.getSimpleName().toLowerCase(Locale.ENGLISH), ContentUriInfo.getPath());
        assertTrue(ContentUriInfo.isValidValue());
    }

    /**
     * Annotated class under test.
     */
    @DefaultContentUri
    private class AnnotatedClassUnderTestNoParams {
    }

    /**
     * Annotated class under test.
     */
    @DefaultContentUri(authority = TEST_AUTHORITY, path = TEST_PATH)
    private class AnnotatedClassUnderTestWithParams {
    }

    /**
     * Non-Annotated class under test.
     */
    private class NonAnnotatedClassUnderTest {
    }

}
