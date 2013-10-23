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
package com.tojc.ormlite.android.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * It is the annotations that are added in OrmLiteContentProvider library. It is
 * also a class to manage their information.
 * @author Jaken
 */
public class AdditionalAnnotation {
    /**
     * This specifies the default ContentUri. If you do not want to use the
     * DefaultContentUri annotation, you must call the
     * MatcherPattern#setContentUri(). "content://authority/path"
     * @see com.tojc.ormlite.android.framework.MatcherPattern#setContentUri(String,
     *      String)
     * @author Jaken
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE })
    public @interface Contract {
        String contractClassName() default "";
    }

    /**
     * This specifies the classes that should be generated into one Contract file, instead of generating one Contract
     * file for each table like the {@link Contract} annotation.
     *
     * @author Michael Cramer
     * @see android.provider.ContactsContract
     */
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.TYPE)
    public @interface SuperContract {
        String contractClassName();
    }

    /**
     * This specifies the default ContentUri. If you do not want to use the
     * DefaultContentUri annotation, you must call the
     * MatcherPattern#setContentUri(). "content://authority/path"
     * @see com.tojc.ormlite.android.framework.MatcherPattern#setContentUri(String,
     *      String)
     * @author Jaken
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE })
    public @interface DefaultContentUri {
        String authority() default "";

        String path() default "";
    }

    /**
     * This specifies the default MIME types(Provider-specific part only and vnd
     * only). If you do not want to use the DefaultContentMimeTypeVnd
     * annotation, you must call the MatcherPattern#setContentMimeTypeVnd().
     * Provider-specific part: "vnd.name.type"
     * @see com.tojc.ormlite.android.framework.MatcherPattern#setContentMimeTypeVnd(String,
     *      String)
     * @author Jaken
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE })
    public @interface DefaultContentMimeTypeVnd {
        String name() default "";

        String type() default "";
    }

    /**
     * Be used query method, if you do not specify how to sort. DefaultSortOrder
     * annotation can be used in more than one field. In that case, specify the
     * sort in ascending order of weight. If you omit the order, it will be in
     * ascending order by default. If you specify the SortOrder.ASC, which
     * explicitly grants the ASC.
     * @author Jaken
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD })
    public @interface DefaultSortOrder {
        SortOrder order() default SortOrder.DEFAULT;

        int weight() default 0;
    }

    /**
     * If you want to replace the column name, then you use the ProjectionMap
     * annotation. You do not need to use it normally. Fields that do not
     * specify a ProjectionMap annotation, set the default column name. If you
     * take advantage of ProjectionMap annotation, use the following in
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
