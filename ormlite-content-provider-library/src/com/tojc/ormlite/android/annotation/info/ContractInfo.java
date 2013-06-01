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

import com.tojc.ormlite.android.annotation.AdditionalAnnotation.Contract;

/**
 * Manage the Contract information.
 * @author Jaken
 */
public class ContractInfo extends AnnotationInfoBase {
    private String contractClassName;

    public ContractInfo(AnnotatedElement element) {
        Contract contract = element.getAnnotation(Contract.class);
        if (contract != null) {
            this.contractClassName = contract.contractClassName();
            validFlagOn();
        }
    }

    public ContractInfo(String contractClassName) {
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
}
