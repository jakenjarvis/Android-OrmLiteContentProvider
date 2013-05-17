package com.tojc.ormlite.android.annotation.info;

import java.lang.reflect.AnnotatedElement;

import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultSortOrder;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.SortOrder;

/**
 * Manage the SortOrder information.
 * @author Jaken
 */
public class SortOrderInfo extends AnnotationInfoBase {
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
        return (fieldname + " " + this.order.toString()).trim();
    }

    @Override
    protected boolean isValidValue() {
        return true;
    }
}
