package com.tojc.ormlite.android.framework;

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.j256.ormlite.field.DatabaseField;

@SmallTest
public class ColumnInfoTest extends AndroidTestCase {
    private static final String TEST_FIELD_NAME = "annotatedField";
    private ColumnInfo columnInfo;

    public void testIsValid_returns_true_when_field_has_empty_column_name() throws NoSuchFieldException {

        // given
        Field field = ClassUnderTest.class.getDeclaredField("annotatedFieldWithEmptyColumnName");

        // when
        columnInfo = new ColumnInfo(field);

        // then
        assertTrue(columnInfo.isValid());
    }

    public void testIsValid_returns_true_when_field_has_no_column_name() throws NoSuchFieldException {

        // given
        Field field = ClassUnderTest.class.getDeclaredField("annotatedField");

        // when
        columnInfo = new ColumnInfo(field);

        // then
        assertTrue(columnInfo.isValid());
    }

    public void testIsValid_returns_true_when_field_has_column_name() throws NoSuchFieldException {

        // given
        Field field = ClassUnderTest.class.getDeclaredField("annotatedFieldWithParams");

        // when
        columnInfo = new ColumnInfo(field);

        // then
        assertTrue(columnInfo.isValid());
    }

    public void testConstructor_throws_exception_when_field_is_not_annotated() throws NoSuchFieldException {

        // given
        Field field = ClassUnderTest.class.getDeclaredField("nonAnnotatedField");

        // when

        // then
        try {
            columnInfo = new ColumnInfo(field);
            fail();
        } catch (Exception e) {
            // test succeeds
        }
    }

    public void testGetProjectionColumnName_returns_field_name_when_has_no_column_name() throws NoSuchFieldException {
        // given
        Field field = ClassUnderTest.class.getDeclaredField(TEST_FIELD_NAME);

        // when
        columnInfo = new ColumnInfo(field);

        // then
        assertEquals(TEST_FIELD_NAME, columnInfo.getColumnName());
    }

    public void testGetProjectionColumnName_returns_field_name_when_has_column_name() throws NoSuchFieldException {
        // given
        Field field = ClassUnderTest.class.getDeclaredField(TEST_FIELD_NAME);

        // when
        columnInfo = new ColumnInfo(field);

        // then
        assertEquals(TEST_FIELD_NAME, columnInfo.getColumnName());
    }

    public void testGetProjectionColumnName_returns_field_name_when_has_empty_column_name() throws NoSuchFieldException {
        final String testFieldName = "annotatedFieldWithEmptyColumnName";
        // given
        Field field = ClassUnderTest.class.getDeclaredField(testFieldName);

        // when
        columnInfo = new ColumnInfo(field);

        // then
        assertEquals(testFieldName, columnInfo.getColumnName());
    }

    // ----------------------------------
    // CLASSES UNDER TEST
    // ----------------------------------
    @SuppressWarnings("unused")
    private class ClassUnderTest {

        private String nonAnnotatedField;

        @DatabaseField
        private String annotatedField;

        @DatabaseField(columnName = StringUtils.EMPTY)
        private String annotatedFieldWithEmptyColumnName;

        @DatabaseField(columnName = TEST_FIELD_NAME)
        private String annotatedFieldWithParams;
    }
}
