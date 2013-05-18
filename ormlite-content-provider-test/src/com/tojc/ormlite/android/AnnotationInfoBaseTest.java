package com.tojc.ormlite.android;

import android.test.AndroidTestCase;

import com.tojc.ormlite.android.annotation.info.AnnotationInfoBase;

public class AnnotationInfoBaseTest extends AndroidTestCase {

    private DummyAnnotationInfoBase dummyAnnotationInfoBase;

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

    /**
     * Class under test.
     * @author SNI
     */
    private class DummyAnnotationInfoBase extends AnnotationInfoBase {

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

    }
}
