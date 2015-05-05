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

import android.test.suitebuilder.annotation.MediumTest;

import com.j256.ormlite.field.DatabaseField;

import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@MediumTest
public class ColumnInfoTest {
    private static final String TEST_FIELD_NAME = "annotatedField";
    private static final String EMPTY = "";
    private ColumnInfo columnInfo;

    @Test
    public void testIsValid_returns_true_when_field_has_empty_column_name()
        throws NoSuchFieldException {

        // given
        Field field = ClassUnderTest.class.getDeclaredField("annotatedFieldWithEmptyColumnName");

        // when
        columnInfo = new ColumnInfo(field);

        // then
        assertTrue(columnInfo.isValid());
    }

    @Test
    public void testIsValid_returns_true_when_field_has_no_column_name() throws NoSuchFieldException {

        // given
        Field field = ClassUnderTest.class.getDeclaredField("annotatedField");

        // when
        columnInfo = new ColumnInfo(field);

        // then
        assertTrue(columnInfo.isValid());
    }

    @Test
    public void testIsValid_returns_true_when_field_has_column_name() throws NoSuchFieldException {

        // given
        Field field = ClassUnderTest.class.getDeclaredField("annotatedFieldWithParams");

        // when
        columnInfo = new ColumnInfo(field);

        // then
        assertTrue(columnInfo.isValid());
    }

    @Test
    public void testConstructor_throws_exception_when_field_is_not_annotated()
        throws NoSuchFieldException {

        // given
        Field field = ClassUnderTest.class.getDeclaredField("nonAnnotatedField");

        // when

        // then
        try {
            columnInfo = new ColumnInfo(field);
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetProjectionColumnName_returns_field_name_when_has_no_column_name()
        throws NoSuchFieldException {
        // given
        Field field = ClassUnderTest.class.getDeclaredField(TEST_FIELD_NAME);

        // when
        columnInfo = new ColumnInfo(field);

        // then
        assertEquals(TEST_FIELD_NAME, columnInfo.getColumnName());
    }

    @Test
    public void testGetProjectionColumnName_returns_field_name_when_has_column_name()
        throws NoSuchFieldException {
        // given
        Field field = ClassUnderTest.class.getDeclaredField(TEST_FIELD_NAME);

        // when
        columnInfo = new ColumnInfo(field);

        // then
        assertEquals(TEST_FIELD_NAME, columnInfo.getColumnName());
    }

    @Test
    public void testGetProjectionColumnName_returns_field_name_when_has_empty_column_name()
        throws NoSuchFieldException {
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

        @DatabaseField(columnName = EMPTY)
        private String annotatedFieldWithEmptyColumnName;

        @DatabaseField(columnName = TEST_FIELD_NAME)
        private String annotatedFieldWithParams;
    }
}
