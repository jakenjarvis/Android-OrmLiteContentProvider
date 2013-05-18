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
    @Target({ElementType.TYPE})
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
    @Target({ElementType.TYPE})
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
    @Target({ElementType.TYPE})
    public @interface DefaultContentMimeTypeVnd {
        String name() default "";

        String type() default "";
    }

    /**
     * Be used query method, if you do not specify how to sort. DefaultSortOrder annotation can be
     * used in more than one field. In that case, specify the sort in ascending order of weight. If
     * you omit the order, it will be in ascending order by default. If you specify the
     * SortOrder.ASC, which explicitly grants the ASC.
     * @author Jaken
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    public @interface DefaultSortOrder {
        SortOrder order() default SortOrder.DEFAULT;

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
    @Target({ElementType.FIELD})
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
        DEFAULT {
            @Override
            public String toString() {
                return "";
            }
        },

        /**
         * Explicitly specify the ASC.
         */
        ASC,

        /**
         * Explicitly specify the DESC.
         */
        DESC;

    }
}
