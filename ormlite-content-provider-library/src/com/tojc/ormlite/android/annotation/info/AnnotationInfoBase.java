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
        String message = this.getClass().getSimpleName() + " class status is abnormal.";
        thowIllegalStateExceptionUnderCondition(throwException && !result, message);
        return result;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    protected final void thowIllegalStateExceptionUnderCondition(boolean condition, String message) {
        if (condition) {
            throw new IllegalStateException(message);
        }
    }
}
