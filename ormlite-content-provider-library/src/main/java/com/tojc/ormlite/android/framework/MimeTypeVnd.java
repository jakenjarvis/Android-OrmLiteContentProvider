/*
 * This file is part of the Android-OrmLiteContentProvider package.
 * 
 * Copyright (c) 2012, Jaken Jarvis (jaken.jarvis@gmail.com)
 * 
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 * 
 * The author may be contacted via 
 * https://github.com/jakenjarvis/Android-OrmLiteContentProvider
 */
package com.tojc.ormlite.android.framework;

import java.io.File;

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
public class MimeTypeVnd implements Validity {
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
