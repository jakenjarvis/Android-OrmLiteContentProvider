package com.tojc.ormlite.android.annotation.info;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.tojc.ormlite.android.framework.Validity;

/**
 * Base class that manages the annotation information.
 * @author Jaken
 */
public abstract class AnnotationInfoBase implements Validity {
    private boolean validFlag = false;

    public AnnotationInfoBase() {
        validFlagOff();
    }

    protected void validFlagOn() {
        this.validFlag = true;
    }

    protected void validFlagOff() {
        this.validFlag = false;
    }

    protected abstract boolean isValidValue();

    @Override
    public boolean isValid() {
        return this.validFlag && isValidValue();
    }

    @Override
    public boolean isValid(boolean throwException) {
        boolean result = this.isValid();
        if (throwException && !result) {
            throw new IllegalStateException(this.getClass().getSimpleName() + " class status is abnormal.");
        }
        return result;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
