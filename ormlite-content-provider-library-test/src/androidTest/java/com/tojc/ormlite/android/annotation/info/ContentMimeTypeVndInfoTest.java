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

import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentMimeTypeVnd;

@SmallTest
public class ContentMimeTypeVndInfoTest extends AndroidTestCase {

    private static final String TEST_NAME = "foo";
    private static final String TEST_TYPE = "bar";
    private static final String EMPTY = "";

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
        contentMimeTypeVndInfo = new ContentMimeTypeVndInfo(TEST_NAME, EMPTY);
        // when

        // then
        assertFalse(contentMimeTypeVndInfo.isValidValue());

        // --
        // given
        contentMimeTypeVndInfo = new ContentMimeTypeVndInfo(EMPTY, TEST_TYPE);
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
        assertEquals(AnnotatedClassUnderTestNoParams.class.getSimpleName().toLowerCase(Locale.ENGLISH), contentMimeTypeVndInfo.getType());
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
        assertEquals(NonAnnotatedClassUnderTest.class.getPackage().getName() + ContentMimeTypeVndInfo.PROVIDER_SUFFIX, contentMimeTypeVndInfo.getName());
        assertEquals(NonAnnotatedClassUnderTest.class.getSimpleName().toLowerCase(Locale.ENGLISH), contentMimeTypeVndInfo.getType());
        assertTrue(contentMimeTypeVndInfo.isValidValue());
    }

    // ----------------------------------
    //  CLASSES UNDER TESTS
    // ----------------------------------
    
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
