package com.tojc.ormlite.android.annotation.info;

import java.lang.reflect.AnnotatedElement;

import org.apache.commons.lang3.StringUtils;

import com.tojc.ormlite.android.annotation.AdditionalAnnotation.ProjectionMap;

/**
 * Manage the ProjectionMap information.
 * @author Jaken
 */
public class ProjectionMapInfo extends AnnotationInfoBase {
    private String name;

    public ProjectionMapInfo(AnnotatedElement element) {
        ProjectionMap projectionMap = element.getAnnotation(ProjectionMap.class);
        if (projectionMap != null) {
            this.name = projectionMap.value();
            validFlagOn();
        }
    }

    public ProjectionMapInfo(String name) {
        this.name = name;
        validFlagOn();
    }

    public String getName() {
        return this.name;
    }

    @Override
    protected boolean isValidValue() {
        return StringUtils.isNotEmpty(name);
    }
}
