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

import android.net.Uri;

import com.tojc.ormlite.android.annotation.info.ContentMimeTypeVndInfo;
import com.tojc.ormlite.android.annotation.info.ContentUriInfo;
import com.tojc.ormlite.android.framework.MimeTypeVnd.SubType;
import com.tojc.ormlite.android.framework.fragment.ContentProviderFragmentInterface;

import java.io.Serializable;

/**
 * Manage the UriMatcher pattern. It holds information related to the pattern code.
 *
 * @author Jaken
 */
public class MatcherPattern implements Serializable, Validity {
    private static final long serialVersionUID = 330324277949148494L;

    private boolean initialized = false;

    private ContentProviderFragmentInterface<?, ?> parentContentProviderFragment;

    private TableInfo tableInfo;
    private SubType subType;
    private String pattern;
    private int patternCode;

    private ContentUriInfo contentUriInfo;
    private ContentMimeTypeVndInfo contentMimeTypeVndInfo;

    private MimeTypeVnd mimeTypeVnd;

    public MatcherPattern(TableInfo tableInfo, SubType subType, String pattern, int patternCode) {
        this.parentContentProviderFragment = null;
        this.tableInfo = tableInfo;
        this.subType = subType;
        this.pattern = pattern;
        this.patternCode = patternCode;

        if (this.tableInfo.getDefaultContentUriInfo().isValid()) {
            this.contentUriInfo = this.tableInfo.getDefaultContentUriInfo();
        } else {
            this.contentUriInfo = null;
        }

        if (this.tableInfo.getDefaultContentMimeTypeVndInfo().isValid()) {
            this.contentMimeTypeVndInfo = this.tableInfo.getDefaultContentMimeTypeVndInfo();
        } else {
            this.contentMimeTypeVndInfo = null;
        }

        if (this.contentMimeTypeVndInfo != null) {
            this.mimeTypeVnd = new MimeTypeVnd(this.subType, this.contentMimeTypeVndInfo);
        } else {
            this.mimeTypeVnd = null;
        }
    }

    @Override
    public boolean isValid() {
        return isValid(false);
    }

    @Override
    public boolean isValid(boolean throwException) {
        boolean result = true;

        if (this.tableInfo == null) {
            result = false;
            if (throwException && !result) {
                throw new IllegalStateException("tableInfo is null.");
            }
        } else if (this.subType == null) {
            result = false;
            if (throwException && !result) {
                throw new IllegalStateException("subType is null.");
            }
        } else if (pattern == null) {
            result = false;
            if (throwException && !result) {
                throw new IllegalStateException("pattern is null.");
            }
        } else if (this.patternCode <= 0) {
            result = false;
            if (throwException && !result) {
                throw new IllegalStateException("patternCode is zero.");
            }
        } else if (this.contentUriInfo == null || !this.contentUriInfo.isValid()) {
            result = false;
            if (throwException && !result) {
                throw new IllegalStateException("contentUriInfo is invalid.");
            }
        } else if (this.contentMimeTypeVndInfo == null || !this.contentMimeTypeVndInfo.isValid()) {
            result = false;
            if (throwException && !result) {
                throw new IllegalStateException("contentMimeTypeVndInfo is invalid.");
            }
        } else if (this.mimeTypeVnd == null || !this.mimeTypeVnd.isValid()) {
            result = false;
            if (throwException && !result) {
                throw new IllegalStateException("mimeTypeVnd is invalid.");
            }
        }
        return result;
    }

    /**
     * Do not call this method. This is only used MatcherController and tests.
     *
     * @see com.tojc.ormlite.android.framework.MatcherController#hasPreinitialized()
     */
    protected void initialize() {
        this.initialized = true;
    }

    /**
     * This will get the ContentProviderFragment that are associated with as a parent.
     *
     * @return Instance of ContentProviderFragment that are associated as a parent
     */
    public ContentProviderFragmentInterface<?, ?> getParentContentProviderFragment() {
        return this.parentContentProviderFragment;
    }

    /**
     * This associates the ContentProviderFragment as a parent.
     *
     * @param parentContentProviderFragment
     */
    public void setParentContentProviderFragment(ContentProviderFragmentInterface<?, ?> parentContentProviderFragment) {
        this.parentContentProviderFragment = parentContentProviderFragment;
    }

    /**
     * Set the ContentUri. This is used when you are not using the DefaultContentUri annotation, or
     * want to override the setting of the DefaultContentUri annotation. This method can not be
     * called after MatcherController#hasPreinitialized().
     *
     * @param contentUriInfo
     * @return Instance of the MatcherPattern class.
     * @see com.tojc.ormlite.android.annotation.info.ContentUriInfo
     * @see com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentUri
     * @see com.tojc.ormlite.android.framework.MatcherController#hasPreinitialized()
     */
    public MatcherPattern setContentUri(ContentUriInfo contentUriInfo) {
        if (this.initialized) {
            throw new IllegalStateException("Can't change the settings after initialization.");
        }
        this.contentUriInfo = contentUriInfo;
        return this;
    }

    /**
     * Set the ContentUri. This is used when you are not using the DefaultContentUri annotation, or
     * want to override the setting of the DefaultContentUri annotation. This method can not be
     * called after MatcherController#hasPreinitialized().
     *
     * @param authority
     * @param path
     * @return Instance of the MatcherPattern class.
     * @see com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentUri
     * @see com.tojc.ormlite.android.framework.MatcherController#hasPreinitialized()
     */
    public MatcherPattern setContentUri(String authority, String path) {
        return this.setContentUri(new ContentUriInfo(authority, path));
    }

    /**
     * Set the MIME types. This is used when you are not using the DefaultContentMimeTypeVnd
     * annotation, or want to override the setting of the DefaultContentMimeTypeVnd annotation. This
     * method can not be called after MatcherController#hasPreinitialized().
     *
     * @param contentMimeTypeVndInfo
     * @return Instance of the MatcherPattern class.
     * @see com.tojc.ormlite.android.annotation.info.ContentMimeTypeVndInfo
     * @see com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentMimeTypeVnd
     * @see com.tojc.ormlite.android.framework.MatcherController#hasPreinitialized()
     */
    public MatcherPattern setContentMimeTypeVnd(ContentMimeTypeVndInfo contentMimeTypeVndInfo) {
        if (this.initialized) {
            throw new IllegalStateException("Can't change the settings after initialization.");
        }
        this.contentMimeTypeVndInfo = contentMimeTypeVndInfo;
        this.mimeTypeVnd = new MimeTypeVnd(this.subType, this.contentMimeTypeVndInfo);
        return this;
    }

    /**
     * Set the MIME types. This is used when you are not using the DefaultContentMimeTypeVnd
     * annotation, or want to override the setting of the DefaultContentMimeTypeVnd annotation. This
     * method can not be called after MatcherController#hasPreinitialized().
     *
     * @param name
     * @param type
     * @return Instance of the MatcherPattern class.
     * @see com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentMimeTypeVnd
     * @see com.tojc.ormlite.android.framework.MatcherController#hasPreinitialized()
     */
    public MatcherPattern setContentMimeTypeVnd(String name, String type) {
        return this.setContentMimeTypeVnd(new ContentMimeTypeVndInfo(name, type));
    }

    public TableInfo getTableInfo() {
        return this.tableInfo;
    }

    public SubType getSubType() {
        return this.subType;
    }

    public String getPattern() {
        return this.pattern;
    }

    public int getPatternCode() {
        return this.patternCode;
    }

    public ContentUriInfo getContentUriInfo() {
        return this.contentUriInfo;
    }

    // public ContentMimeTypeVndInfo getContentMimeTypeVndInfo()
    // {
    // return this.contentMimeTypeVndInfo;
    // }

    public MimeTypeVnd getMimeTypeVnd() {
        return this.mimeTypeVnd;
    }

    /**
     * @return Return the concatenation string of Path and Pattern from ContentUri. <br>
     * ex)<br>
     * <code>
     * ContentUri = "content://com.example.app.provider/table2/dataset1"<br>
     * Return = "table2/dataset1"<br>
     * </code>
     */
    public String getPathAndPatternString() {
        return this.contentUriInfo.getPath() + "/" + this.pattern;
    }

    /**
     * @return Returns the full ContentUri.(Uri object)
     */
    public Uri getContentUriPattern() {
        return Uri.parse(this.contentUriInfo.getContentUri() + "/" + this.pattern);
    }

    /**
     * @return Returns the full MIME types string. <br>
     * ex)<br>
     * <code>
     * Return = "vnd.android.cursor.item/vnd.com.example.provider.table1"<br>
     * </code>
     */
    public String getMimeTypeVndString() {
        return this.mimeTypeVnd.getMimeTypeString();
    }

    @Override
    public String toString() {
        return getContentUriPattern().toString();
    }

}
