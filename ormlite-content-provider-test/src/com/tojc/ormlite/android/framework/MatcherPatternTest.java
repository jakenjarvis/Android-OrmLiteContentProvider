package com.tojc.ormlite.android.framework;

import org.apache.commons.lang3.StringUtils;

import android.provider.BaseColumns;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.j256.ormlite.field.DatabaseField;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentMimeTypeVnd;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentUri;
import com.tojc.ormlite.android.framework.MimeTypeVnd.SubType;

@SmallTest
public class MatcherPatternTest extends AndroidTestCase {

    private static final String TEST_PATTERN = "foo";

    // activate this to debug tests
    private static final boolean DEBUG = false;

    private MatcherPattern matcherPattern;

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

    public void testIsValid_should_return_true_if_pattern_is_empty() {
        // given
        TableInfo tableInfo = new TableInfo(ClassUnderTestWithAnnotations.class);
        SubType subType = SubType.ITEM;
        String pattern = StringUtils.EMPTY;
        int patternCode = 1;

        // when
        matcherPattern = new MatcherPattern(tableInfo, subType, pattern, patternCode);

        // then
        assertTrue(matcherPattern.isValid(DEBUG));
    }

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
