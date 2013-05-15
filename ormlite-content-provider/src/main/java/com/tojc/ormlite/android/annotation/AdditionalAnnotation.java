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
package com.tojc.ormlite.android.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

import android.content.ContentResolver;
import android.net.Uri;

import com.tojc.ormlite.android.framework.Validity;

/**
 * It is the annotations that are added in OrmLiteContentProvider library. It is also a class to
 * manage their information.
 * @author Jaken
 */
public class AdditionalAnnotation {
    /**
     * This specifies the default ContentUri. If you do not want to use the DefaultContentUri
     * annotation, you must call the MatcherPattern#setContentUri(). "content://authority/path"
     * @see com.tojc.ormlite.android.framework.MatcherPattern#setContentUri(String, String)
     * @author Jaken
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE })
    public @interface Contract {
        String contractClassName() default "";
    }

    /**
     * This specifies the default ContentUri. If you do not want to use the DefaultContentUri
     * annotation, you must call the MatcherPattern#setContentUri(). "content://authority/path"
     * @see com.tojc.ormlite.android.framework.MatcherPattern#setContentUri(String, String)
     * @author Jaken
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE })
    public @interface DefaultContentUri {
        String authority() default "";

        String path() default "";
    }

    /**
     * This specifies the default MIME types(Provider-specific part only and vnd only). If you do
     * not want to use the DefaultContentMimeTypeVnd annotation, you must call the
     * MatcherPattern#setContentMimeTypeVnd(). Provider-specific part: "vnd.name.type"
     * @see com.tojc.ormlite.android.framework.MatcherPattern#setContentMimeTypeVnd(String, String)
     * @author Jaken
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE })
    public @interface DefaultContentMimeTypeVnd {
        String name() default "";

        String type() default "";
    }

    /**
     * Be used query method, if you do not specify how to sort. DefaultSortOrder annotation can be
     * used in more than one field. In that case, specify the sort in ascending order of weight. If
     * you omit the order, it will be in ascending order by default. If you specify the
     * SortOrder.Asc, which explicitly grants the ASC.
     * @author Jaken
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD })
    public @interface DefaultSortOrder {
        SortOrder order() default SortOrder.Default;

        int weight() default 0;
    }

    /**
     * If you want to replace the column name, then you use the ProjectionMap annotation. You do not
     * need to use it normally. Fields that do not specify a ProjectionMap annotation, set the
     * default column name. If you take advantage of ProjectionMap annotation, use the following in
     * onQuery(). <br>
     * ex)<br>
     * <code>
     *         SQLiteQueryBuilder builder = new SQLiteQueryBuilder();<br>
     *         builder.setProjectionMap(target.getTableInfo().getProjectionMap());<br>
     *     </code>
     * @author Jaken
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD })
    public @interface ProjectionMap {
        String value();
    }

    /**
     * Represents the SortOrder.
     * @author Jaken
     */
    public enum SortOrder {
        /**
         * Are treated the same as ASC. (dependent SQLite)
         */
        Default(""),

        /**
         * Explicitly specify the ASC.
         */
        Asc("ASC"),

        /**
         * Explicitly specify the DESC.
         */
        Desc("DESC");

        private SortOrder(String name) {
            this.name = name;
        }

        private final String name;

        @Override
        public String toString() {
            return this.name;
        }
    }

    /**
     * Base class that manages the annotation information.
     * @author Jaken
     */
    public static abstract class AnnotationInfoBase implements Validity {
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
    }

    /**
     * Manage the Contract information.
     * @author Jaken
     */
    public static class ContractInfo extends AnnotationInfoBase {
        private String contractClassName;

        public ContractInfo(AnnotatedElement element) {
            super();
            Contract contract = element.getAnnotation(Contract.class);
            if (contract != null) {
                this.contractClassName = contract.contractClassName();
                validFlagOn();
            }
        }

        public ContractInfo(String contractClassName) {
            super();
            this.contractClassName = contractClassName;
            validFlagOn();
        }

        public String getContractClassName() {
            return this.contractClassName;
        }

        @Override
        protected boolean isValidValue() {
            return true;
        }

        @Override
        public String toString() {
            return "[contractClassName=" + contractClassName.toString() + "]";
        }
    }

    /**
     * Manage the ContentUri information.
     * @author Jaken
     */
    public static class ContentUriInfo extends AnnotationInfoBase {
        private String authority;
        private String path;

        public ContentUriInfo(AnnotatedElement element) {
            super();
            DefaultContentUri contentUri = element.getAnnotation(DefaultContentUri.class);
            if (contentUri != null) {
                this.authority = contentUri.authority();
                this.path = contentUri.path();
                validFlagOn();
            }
        }

        public ContentUriInfo(String authority, String path) {
            super();
            this.authority = authority;
            this.path = path;
            validFlagOn();
        }

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
            return this.authority.length() >= 1 && this.path.length() >= 1;
        }

        @Override
        public String toString() {
            return getContentUri().toString();
        }
    }

    /**
     * Manage the MIME Types information.
     * @author Jaken
     */
    public static class ContentMimeTypeVndInfo extends AnnotationInfoBase {
        public static final String VND = "vnd";

        private String name;
        private String type;

        public ContentMimeTypeVndInfo(AnnotatedElement element) {
            super();
            DefaultContentMimeTypeVnd contentMimeTypeVnd = element.getAnnotation(DefaultContentMimeTypeVnd.class);
            if (contentMimeTypeVnd != null) {
                this.name = contentMimeTypeVnd.name();
                this.type = contentMimeTypeVnd.type();
                validFlagOn();
            }
        }

        public ContentMimeTypeVndInfo(String name, String type) {
            super();
            this.name = name;
            this.type = type;
            validFlagOn();
        }

        public String getName() {
            return this.name;
        }

        public String getType() {
            return this.type;
        }

        public String getVndProviderSpecificString() {
            return VND + "." + this.name + "." + this.type;
        }

        @Override
        protected boolean isValidValue() {
            return this.name.length() >= 1 && this.type.length() >= 1;
        }

        @Override
        public String toString() {
            return getVndProviderSpecificString();
        }
    }

    /**
     * Manage the SortOrder information.
     * @author Jaken
     */
    public static class SortOrderInfo extends AnnotationInfoBase {
        private SortOrder order;
        private int weight;

        public SortOrderInfo(AnnotatedElement element) {
            super();
            DefaultSortOrder defaultSortOrder = element.getAnnotation(DefaultSortOrder.class);
            if (defaultSortOrder != null) {
                this.order = defaultSortOrder.order();
                this.weight = defaultSortOrder.weight();
                validFlagOn();
            }
        }

        public SortOrderInfo(SortOrder order, int weight) {
            super();
            this.order = order;
            this.weight = weight;
            validFlagOn();
        }

        public SortOrder getOrder() {
            return this.order;
        }

        public int getWeight() {
            return this.weight;
        }

        public String makeSqlOrderString(String fieldname) {
            return (fieldname + " " + this.order.toString()).trim();
        }

        @Override
        protected boolean isValidValue() {
            return true;
        }

        @Override
        public String toString() {
            return this.order.toString();
        }
    }

    /**
     * Manage the ProjectionMap information.
     * @author Jaken
     */
    public static class ProjectionMapInfo extends AnnotationInfoBase {
        private String name;

        public ProjectionMapInfo(AnnotatedElement element) {
            super();
            ProjectionMap projectionMap = element.getAnnotation(ProjectionMap.class);
            if (projectionMap != null) {
                this.name = projectionMap.value();
                validFlagOn();
            }
        }

        public ProjectionMapInfo(String name) {
            super();
            this.name = name;
            validFlagOn();
        }

        public String getName() {
            return this.name;
        }

        @Override
        protected boolean isValidValue() {
            return this.name.length() >= 1;
        }

        @Override
        public String toString() {
            return "";
        }
    }

}
