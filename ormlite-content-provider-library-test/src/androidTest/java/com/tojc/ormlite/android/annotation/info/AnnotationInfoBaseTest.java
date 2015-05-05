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

import android.test.suitebuilder.annotation.MediumTest;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@MediumTest
public class AnnotationInfoBaseTest {

    private DummyAnnotationInfoBase dummyAnnotationInfoBase;

    @Test
    public void testIsValid_should_return_false_until_validated() {
        // given
        dummyAnnotationInfoBase = new DummyAnnotationInfoBase(true);

        // when

        // then
        assertFalse(dummyAnnotationInfoBase.isValid());

        // when
        dummyAnnotationInfoBase.validate();

        // then
        assertTrue(dummyAnnotationInfoBase.isValid());
    }

    @Test
    public void testIsValid_throws_exception_or_not() {
        // given
        dummyAnnotationInfoBase = new DummyAnnotationInfoBase(true);

        // when

        // then
        assertFalse(dummyAnnotationInfoBase.isValid(false));

        // when

        // then
        try {
            dummyAnnotationInfoBase.isValid(true);
            fail();
        } catch (Exception ex) {
            assertTrue(true);
        }
    }

    /**
     * Class under test.
     *
     * @author SNI
     */
    private static class DummyAnnotationInfoBase extends AnnotationInfoBase {

        private boolean isValidValue;

        public DummyAnnotationInfoBase(boolean isValidValue) {
            this.isValidValue = isValidValue;
        }

        public void validate() {
            validFlagOn();
        }

        @Override
        protected boolean isValidValue() {
            return isValidValue;
        }

        @Override
        public String toString() {
            return "DummyAnnotationInfoBase{"
                    + "isValidValue=" + isValidValue
                    + "} " + super.toString();
        }
    }
}
