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

import java.io.File;
import java.io.Serializable;

import android.content.ContentResolver;

import com.tojc.ormlite.android.annotation.info.ContentMimeTypeVndInfo;

/**
 * Manage the MIME types information. It provides full support for ContentProvider MIME Types.
 * vnd.android.cursor.dir/vnd.com.example.provider.table1
 * vnd.android.cursor.item/vnd.com.example.provider.table1 Type part : vnd Subtype part : If the URI
 * pattern is for a single row: android.cursor.item/ If the URI pattern is for more than one row:
 * android.cursor.dir/ Provider-specific part: vnd.name.type (Manage the ContentMimeTypeVndInfo
 * class)
 * @see com.tojc.ormlite.android.framework.MimeTypeVnd.SubType
 * @see com.tojc.ormlite.android.annotation.info.ContentMimeTypeVndInfo
 * @author Jaken
 */
public class MimeTypeVnd implements Serializable, Validity {
    private static final long serialVersionUID = -5451386892898706057L;

    public enum SubType {
        ITEM(ContentResolver.CURSOR_ITEM_BASE_TYPE), //
        DIRECTORY(ContentResolver.CURSOR_DIR_BASE_TYPE);

        private SubType(String name) {
            this.name = name;
        }

        private final String name;

        @Override
        public String toString() {
            return this.name;
        }
    }

    private SubType subType;
    private ContentMimeTypeVndInfo providerSpecific;

    public MimeTypeVnd(SubType subType, ContentMimeTypeVndInfo providerSpecific) {
        this.subType = subType;
        this.providerSpecific = providerSpecific;
    }

    @Override
    public boolean isValid() {
        return isValid(false);
    }

    @Override
    public boolean isValid(boolean throwException) {
        boolean result = true;

        if (this.subType == null) {
            result = false;
            if (throwException && !result) {
                throw new IllegalStateException("subType is null.");
            }
        } else if (!this.providerSpecific.isValid()) {
            result = false;
            if (throwException && !result) {
                throw new IllegalStateException("providerSpecific is invalid.");
            }
        }
        return result;
    }

    public SubType getSubType() {
        return this.subType;
    }

    public void setSubType(SubType subType) {
        this.subType = subType;
    }

    public ContentMimeTypeVndInfo getProviderSpecific() {
        return this.providerSpecific;
    }

    public void setProviderSpecific(ContentMimeTypeVndInfo providerSpecific) {
        this.providerSpecific = providerSpecific;
    }

    public String getMimeTypeString() {
        return this.subType.toString() + File.separator + this.providerSpecific.getVndProviderSpecificString();
    }

    @Override
    public String toString() {
        return getMimeTypeString();
    }

}
