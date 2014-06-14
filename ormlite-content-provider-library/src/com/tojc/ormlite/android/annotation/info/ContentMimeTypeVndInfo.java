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

import java.io.Serializable;
import java.lang.reflect.AnnotatedElement;

import android.text.TextUtils;

import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentMimeTypeVnd;

/**
 * Manage the MIME Types information.
 * @author Jaken
 */
public class ContentMimeTypeVndInfo extends AnnotationInfoBase implements Serializable {
    private static final long serialVersionUID = 2111248452176376223L;

    // ----------------------------------
    // CONSTANTS
    // ----------------------------------
    public static final String VND = "vnd";
    public static final String PROVIDER_SUFFIX = ".provider";
    public static final String VND_SEPARATOR = ".";

    // ----------------------------------
    // ATRRIBUTES
    // ----------------------------------
    private String name;
    private String type;

    // ----------------------------------
    // CONSTRUCTORS
    // ----------------------------------

    public ContentMimeTypeVndInfo(AnnotatedElement element) {
        DefaultContentMimeTypeVnd contentMimeTypeVnd = element.getAnnotation(DefaultContentMimeTypeVnd.class);
        String name = null;
        String type = null;
        if (contentMimeTypeVnd != null) {
            name = contentMimeTypeVnd.name();
            type = contentMimeTypeVnd.type();
        }

        if (element instanceof Class<?>) {
            Class<?> clazz = (Class<?>) element;
            if (TextUtils.isEmpty(name)) {
                name = clazz.getPackage().getName() + PROVIDER_SUFFIX;
            }

            if (TextUtils.isEmpty(type)) {
                type = clazz.getSimpleName().toLowerCase();
            }
        }

        initialize(name, type);
    }

    public ContentMimeTypeVndInfo(String name, String type) {
        initialize(name, type);
    }

    // ----------------------------------
    // PUBLIC METHODS
    // ----------------------------------
    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public String getVndProviderSpecificString() {
        return VND + VND_SEPARATOR + this.name + VND_SEPARATOR + this.type;
    }

    @Override
    protected boolean isValidValue() {
        return !TextUtils.isEmpty(this.name) && !TextUtils.isEmpty(this.type);
    }

    // ----------------------------------
    // PRIVATE METHODS
    // ----------------------------------
    private void initialize(String name, String type) {
        this.name = name;
        this.type = type;
        validFlagOn();
    }

    @Override
    public String toString() {
        return "ContentMimeTypeVndInfo{"
                + "name='" + name + '\''
                + ", type='" + type + '\''
                + "} " + super.toString();
    }
}
