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

import android.provider.BaseColumns;
import android.test.suitebuilder.annotation.MediumTest;

import com.j256.ormlite.field.DatabaseField;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentMimeTypeVnd;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentUri;
import com.tojc.ormlite.android.framework.MimeTypeVnd.SubType;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@MediumTest
public class MatcherPatternTest {

    private static final String TEST_PATTERN = "foo";

    // activate this to debug tests
    private static final boolean DEBUG = false;
    private static final String EMPTY = "";

    private MatcherPattern matcherPattern;

    @Test
    public void testIsValid_should_return_false_if_subtype_is_null() {
        // given
        TableInfo tableInfo = new TableInfo(ClassUnderTestWithAnnotations.class);
        SubType subType = null;
        String pattern = TEST_PATTERN;
        int patternCode = 1;

        // when
        matcherPattern = new MatcherPattern(tableInfo, subType, pattern, patternCode);

        // then
        assertFalse(matcherPattern.isValid(DEBUG));
    }

    @Test
    public void testIsValid_should_return_false_if_pattern_is_null() {
        // given
        TableInfo tableInfo = new TableInfo(ClassUnderTestWithAnnotations.class);
        SubType subType = SubType.ITEM;
        String pattern = null;
        int patternCode = 1;

        // when
        matcherPattern = new MatcherPattern(tableInfo, subType, pattern, patternCode);

        // then
        assertFalse(matcherPattern.isValid(DEBUG));
    }

    @Test
    public void testIsValid_should_return_true_if_pattern_is_empty() {
        // given
        TableInfo tableInfo = new TableInfo(ClassUnderTestWithAnnotations.class);
        SubType subType = SubType.ITEM;
        String pattern = EMPTY;
        int patternCode = 1;

        // when
        matcherPattern = new MatcherPattern(tableInfo, subType, pattern, patternCode);

        // then
        assertTrue(matcherPattern.isValid(DEBUG));
    }

    @Test
    public void testIsValid_should_return_false_if_pattern_code_is_0_or_negative() {
        // given
        TableInfo tableInfo = new TableInfo(ClassUnderTestWithAnnotations.class);
        SubType subType = SubType.ITEM;
        String pattern = TEST_PATTERN;
        int patternCode = 0;

        // when
        matcherPattern = new MatcherPattern(tableInfo, subType, pattern, patternCode);

        // then
        assertFalse(matcherPattern.isValid(DEBUG));

        // --
        // given
        patternCode = -1;

        // when
        matcherPattern = new MatcherPattern(tableInfo, subType, pattern, patternCode);

        // then
        assertFalse(matcherPattern.isValid(DEBUG));
    }

    @Test
    public void testIsValid_should_return_true_for_properly_defined_matcher_pattern() {
        // given
        TableInfo tableInfo = new TableInfo(ClassUnderTestWithAnnotations.class);
        SubType subType = SubType.ITEM;
        String pattern = TEST_PATTERN;
        int patternCode = 1;

        // when
        matcherPattern = new MatcherPattern(tableInfo, subType, pattern, patternCode);

        // then
        assertTrue(matcherPattern.isValid());
    }

    @Test
    public void testIsValid_should_return_true_for_non_annotated_class() {
        // given
        TableInfo tableInfo = new TableInfo(ClassUnderTestWithoutAnnotations.class);
        SubType subType = SubType.ITEM;
        String pattern = TEST_PATTERN;
        int patternCode = 1;

        // when
        matcherPattern = new MatcherPattern(tableInfo, subType, pattern, patternCode);

        // then
        assertTrue(matcherPattern.isValid());
    }

    @Test
    public void testIsValid_should_return_true_for_class_annotated_with_params() {
        // given
        TableInfo tableInfo = new TableInfo(ClassUnderTestWithAnnotationsWithParams.class);
        SubType subType = SubType.ITEM;
        String pattern = TEST_PATTERN;
        int patternCode = 1;

        // when
        matcherPattern = new MatcherPattern(tableInfo, subType, pattern, patternCode);

        // then
        assertTrue(matcherPattern.isValid());
    }

    // ----------------------------------
    // CLASSES UNDER TESTS
    // ----------------------------------
    @DefaultContentUri
    @DefaultContentMimeTypeVnd
    private class ClassUnderTestWithAnnotations {

        @DatabaseField(columnName = BaseColumns._ID, generatedId = true)
        private int id;
    }

    private class ClassUnderTestWithoutAnnotations {

        @DatabaseField(columnName = BaseColumns._ID, generatedId = true)
        private int id;
    }

    @DefaultContentUri(authority = "foo", path = "bar")
    @DefaultContentMimeTypeVnd(name = "qux", type = "quux")
    private class ClassUnderTestWithAnnotationsWithParams {

        @DatabaseField(columnName = BaseColumns._ID, generatedId = true)
        private int id;
    }

}
