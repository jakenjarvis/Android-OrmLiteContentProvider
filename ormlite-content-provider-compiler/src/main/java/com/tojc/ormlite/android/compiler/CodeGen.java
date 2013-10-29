/*
 * Copyright (C) 2012 Square, Inc.
 *
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
 */
package com.tojc.ormlite.android.compiler;

import com.tojc.ormlite.android.annotation.AdditionalAnnotation.Contract;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Support for annotation processors.
 */
final class CodeGen {
    private static final Comparator<Element> ELEMENT_COMPARATOR = new Comparator<Element>() {
        @Override
        public int compare(final Element o1, final Element o2) {
            if (o1 != null) {
                final String className1 = o1.getSimpleName().toString();
                final String className2 = o2.getSimpleName().toString();
                return className1.compareTo(className2);
            } else {
                if (o2 == null) {
                    return 0;
                } else {
                    return -1;
                }
            }
        }
    };

    private CodeGen() {
    }

    public static PackageElement getPackage(Element type) {
        while (type.getKind() != ElementKind.PACKAGE) {
            type = type.getEnclosingElement();
        }
        return (PackageElement) type;
    }

    static List<Element> getAllElementsAnnotatedWith(final Class<? extends Annotation> class1, final Element classElement) {
        final List<Element> allFields = getEnclosedElements(classElement, ElementKind.FIELD);
        final List<Element> result = new ArrayList<Element>(allFields.size());
        for (final Element fieldElement : allFields) {
            if (fieldElement.getAnnotation(class1) != null) {
                result.add(fieldElement);
            }
        }
        return result;
    }

    private static List<Element> getEnclosedElements(final Element element, final ElementKind elementKind) {
        final List<? extends Element> enclosedElements = element.getEnclosedElements();
        final List<Element> list = new ArrayList<Element>(enclosedElements.size());
        for (final Element enclosedElement : enclosedElements) {
            if (enclosedElement.getKind() == elementKind) {
                list.add(enclosedElement);
            }
        }
        return list;
    }

    static Map<String, Set<Element>> groupElementsByContractClassName(final Collection<? extends Element> classElements) {
        final Map<String, Set<Element>> grouped = new HashMap<String, Set<Element>>(classElements.size());
        for (final Element classElement : classElements) {
            final Contract contractAnnotation = classElement.getAnnotation(Contract.class);
            final String contractClassName = contractAnnotation.contractClassName();

            final String targetClassName;
            if (contractClassName == null || contractClassName.isEmpty()) {
                final PackageElement targetPackage;
                targetPackage = getPackage(classElement);
                targetClassName = targetPackage.getQualifiedName().toString() + '.' + classElement.getSimpleName() + "Contract";
            } else {
                targetClassName = contractClassName;
            }

            if (grouped.containsKey(targetClassName)) {
                grouped.get(targetClassName).add(classElement);
            } else {
                final Set<Element> elements = new TreeSet<Element>(ELEMENT_COMPARATOR);
                elements.add(classElement);
                grouped.put(targetClassName, elements);
            }
        }
        return grouped;
    }
}
