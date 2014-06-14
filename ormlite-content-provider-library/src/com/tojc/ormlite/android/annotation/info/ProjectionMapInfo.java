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

import java.io.Serializable;
import java.lang.reflect.AnnotatedElement;

import android.text.TextUtils;

import com.tojc.ormlite.android.annotation.AdditionalAnnotation.ProjectionMap;

/**
 * Manage the ProjectionMap information.
 * @author Jaken
 */
public class ProjectionMapInfo extends AnnotationInfoBase implements Serializable {
    private static final long serialVersionUID = -979022110690648355L;

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
        return !TextUtils.isEmpty(name);
    }

    @Override
    public String toString() {
        return "ProjectionMapInfo{"
                + "name='" + name + '\''
                + "} " + super.toString();
    }
}
