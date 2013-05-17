/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 */

package com.tojc.ormlite.android.compiler;

import java.util.List;

import javax.lang.model.type.TypeMirror;

/**
 * Container class for method information
 * @author <a href=\"mailto:christoffer@christoffer.me\">Christoffer Pettersson</a>
 */

final class MethodData {

    private TypeMirror returnType;
    private List<? extends TypeMirror> parameterTypes;
    private List<? extends TypeMirror> thrownTypes;

    public void setParameterTypes(final List<? extends TypeMirror> parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public List<? extends TypeMirror> getParameterTypes() {
        return parameterTypes;
    }

    public TypeMirror getReturnType() {
        return returnType;
    }

    public void setReturnType(final TypeMirror returnType) {
        this.returnType = returnType;
    }

    public void setThrownTypes(final List<? extends TypeMirror> thrownTypes) {
        this.thrownTypes = thrownTypes;
    }

    public List<? extends TypeMirror> getThrownTypes() {
        return thrownTypes;
    }

}
