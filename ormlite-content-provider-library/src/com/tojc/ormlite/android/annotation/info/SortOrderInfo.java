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

import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultSortOrder;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.SortOrder;

/**
 * Manage the SortOrder information.
 * @author Jaken
 */
public class SortOrderInfo extends AnnotationInfoBase {
    private static final String SQL_ORDER_SEPARATOR = " ";

    private SortOrder order;
    private int weight;

    public SortOrderInfo(AnnotatedElement element) {
        DefaultSortOrder defaultSortOrder = element.getAnnotation(DefaultSortOrder.class);
        if (defaultSortOrder != null) {
            this.order = defaultSortOrder.order();
            this.weight = defaultSortOrder.weight();
            validFlagOn();
        }
    }

    public SortOrderInfo(SortOrder order, int weight) {
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
        return (fieldname + SQL_ORDER_SEPARATOR + this.order.toString()).trim();
    }

    @Override
    protected boolean isValidValue() {
        return true;
    }

    @Override
    public String toString() {
        return "SortOrderInfo{"
                + "order=" + order
                + ", weight=" + weight
                + "} " + super.toString();
    }
}
