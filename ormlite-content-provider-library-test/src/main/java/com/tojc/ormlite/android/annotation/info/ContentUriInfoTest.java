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
package com.tojc.ormlite.android.annotation.info;

import java.util.Locale;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentUri;

@SmallTest
public class ContentUriInfoTest extends AndroidTestCase {

    private static final String TEST_AUTHORITY = "foo";
    private static final String TEST_PATH = "bar";
    private static final String EMPTY = "";

    private ContentUriInfo contentUriInfo;

    public void testIsValidValue_returns_false_for_null_or_empty_package_or_class() {
        // given
        contentUriInfo = new ContentUriInfo(null, null);
        // when

        // then
        assertFalse(contentUriInfo.isValidValue());

        // --
        // given
        contentUriInfo = new ContentUriInfo(null, TEST_PATH);
        // when

        // then
        assertFalse(contentUriInfo.isValidValue());

        // --
        // given
        contentUriInfo = new ContentUriInfo(TEST_AUTHORITY, null);
        // when

        // then
        assertFalse(contentUriInfo.isValidValue());

        // --
        // given
        contentUriInfo = new ContentUriInfo(TEST_AUTHORITY, EMPTY);
        // when

        // then
        assertFalse(contentUriInfo.isValidValue());

        // --
        // given
        contentUriInfo = new ContentUriInfo(EMPTY, TEST_PATH);
        // when

        // then
        assertFalse(contentUriInfo.isValidValue());
    }

    public void testIsValidValue_returns_true() {
        // given
        contentUriInfo = new ContentUriInfo(TEST_AUTHORITY, TEST_PATH);
        // when

        // then
        assertTrue(contentUriInfo.isValidValue());
    }

    public void testgetAuthority() {
        // given
        contentUriInfo = new ContentUriInfo(TEST_AUTHORITY, TEST_PATH);
        // when

        // then
        assertEquals(TEST_AUTHORITY, contentUriInfo.getAuthority());
    }

    public void testgetPath() {
        // given
        contentUriInfo = new ContentUriInfo(TEST_AUTHORITY, TEST_PATH);
        // when

        // then
        assertEquals(TEST_PATH, contentUriInfo.getPath());
    }

    public void testIsValidValue_returns_right_values_for_annotated_element_without_params() {
        // given
        contentUriInfo = new ContentUriInfo(AnnotatedClassUnderTestNoParams.class);
        // when

        // then
        assertEquals(AnnotatedClassUnderTestNoParams.class.getPackage().getName(), contentUriInfo.getAuthority());
        assertEquals(AnnotatedClassUnderTestNoParams.class.getSimpleName().toLowerCase(Locale.ENGLISH), contentUriInfo.getPath());
        assertTrue(contentUriInfo.isValidValue());
    }

    public void testIsValidValue_returns_right_values_for_annotated_element_with_params() {
        // given
        contentUriInfo = new ContentUriInfo(AnnotatedClassUnderTestWithParams.class);
        // when

        // then
        assertEquals(TEST_AUTHORITY, contentUriInfo.getAuthority());
        assertEquals(TEST_PATH, contentUriInfo.getPath());
        assertTrue(contentUriInfo.isValidValue());
    }

    public void testIsValidValue_returns_right_values_for_non_annotated_element() {
        // given
        contentUriInfo = new ContentUriInfo(NonAnnotatedClassUnderTest.class);
        // when

        // then
        assertEquals(NonAnnotatedClassUnderTest.class.getPackage().getName(), contentUriInfo.getAuthority());
        assertEquals(NonAnnotatedClassUnderTest.class.getSimpleName().toLowerCase(Locale.ENGLISH), contentUriInfo.getPath());
        assertTrue(contentUriInfo.isValidValue());
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
