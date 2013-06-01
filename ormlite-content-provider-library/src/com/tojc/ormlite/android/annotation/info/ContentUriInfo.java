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

import java.lang.reflect.AnnotatedElement;

import org.apache.commons.lang3.StringUtils;

import android.content.ContentResolver;
import android.net.Uri;

import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentUri;

/**
 * Manage the ContentUri information.
 * @author Jaken
 */
public class ContentUriInfo extends AnnotationInfoBase {
    // ----------------------------------
    // ATTRIBUTES
    // ----------------------------------
    private String authority;
    private String path;

    // ----------------------------------
    // CONSTRUCTORS
    // ----------------------------------
    public ContentUriInfo(AnnotatedElement element) {
        DefaultContentUri contentUri = element.getAnnotation(DefaultContentUri.class);
        String authority = null;
        String path = null;
        if (contentUri != null) {
            authority = contentUri.authority();
            path = contentUri.path();
        }

        if (element instanceof Class<?>) {
            Class<?> clazz = (Class<?>) element;
            if (StringUtils.isEmpty(authority)) {
                authority = clazz.getPackage().getName();
            }
            if (StringUtils.isEmpty(path)) {
                // TODO use DataBase annotation
                path = clazz.getSimpleName().toLowerCase();
            }
        }

        initialize(authority, path);
    }

    public ContentUriInfo(String authority, String path) {
        initialize(authority, path);
    }

    // ----------------------------------
    // PUBLIC METHODS
    // ----------------------------------
    public String getAuthority() {
        return this.authority;
    }

    public String getPath() {
        return this.path;
    }

    public Uri getContentUri() {
        return new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(this.authority).appendPath(this.path).build();
    }

    @Override
    protected boolean isValidValue() {
        return StringUtils.isNotEmpty(this.authority) && StringUtils.isNotEmpty(this.path);
    }

    // ----------------------------------
    // PRIVATE METHODS
    // ----------------------------------
    private void initialize(String authority, String path) {
        this.authority = authority;
        this.path = path;
        validFlagOn();
    }
}
