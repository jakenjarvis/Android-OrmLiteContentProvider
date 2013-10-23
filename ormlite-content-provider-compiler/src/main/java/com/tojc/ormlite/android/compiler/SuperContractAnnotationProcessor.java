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

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.squareup.javawriter.JavaWriter;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentMimeTypeVnd;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentUri;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.SuperContract;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

/**
 * SuperContract annotation processor
 *
 * @author Michael Cramer
 * @see SuperContract
 * @since 1.0.4
 */
@SupportedAnnotationTypes("com.tojc.ormlite.android.annotation.AdditionalAnnotation.SuperContract")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class SuperContractAnnotationProcessor extends AbstractProcessor implements Comparator<Element>, Serializable {
    private static final String DEFAULT_CONTENT_URI_STATEMENT = "new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(AUTHORITY).appendPath(CONTENT_URI_PATH).build()";
    private static final String SUPER_MIME_TYPE_NAME = "SUPER_MIME_TYPE_NAME";
    private static final String SUPER_AUTHORITY = "SUPER_AUTHORITY";
    private static final int COUNT_FACTOR = 10;
    private int contractCount = 0;

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnvironment) {
        // Get all classes that has the annotation
        final Set<? extends Element> annotatedElements = roundEnvironment.getElementsAnnotatedWith(SuperContract.class);

        // Get the annotation information grouped by SuperContractClassName
        final Map<String, Set<Element>> grouped = groupElementsByContractClassName(annotatedElements);

        // process every SuperContractClassName
        for (final Map.Entry<String, Set<Element>> groupedElements : grouped.entrySet()) {
            final String targetClassName = groupedElements.getKey();
            final Set<Element> classElements = groupedElements.getValue();

            final String targetPackageName = targetClassName.substring(0, targetClassName.lastIndexOf('.'));

            Writer out = null;
            try {
                final JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(targetClassName, (Element[]) null);

                out = sourceFile.openWriter();
                final JavaWriter writer = new JavaWriter(out);

                // package and import statements
                writer.emitPackage(targetPackageName)
                        .emitImports("android.net.Uri")
                        .emitImports("android.content.ContentResolver")
                        .emitImports("android.provider.BaseColumns")
                        .emitEmptyLine();

                // super contract class with private constructor
                writer.beginType(targetClassName, "class", EnumSet.of(PUBLIC, FINAL))
                        .emitField("String", SUPER_AUTHORITY, EnumSet.of(STATIC, PRIVATE, FINAL), JavaWriter.stringLiteral(targetPackageName))
                        .emitField("String", SUPER_MIME_TYPE_NAME, EnumSet.of(STATIC, PRIVATE, FINAL), JavaWriter.stringLiteral(targetPackageName + ".provider"))
                        .emitEmptyLine()
                        .beginMethod(null, targetClassName, EnumSet.of(PRIVATE))
                        .endMethod()
                        .emitEmptyLine();

                // For each class that has the annotation
                final Iterator<Element> iterator = classElements.iterator();
                while (iterator.hasNext()) {
                    final Element classElement = iterator.next();
                    int patternCode = 1;

                    final DefaultContentUri defaultContentUriAnnotation = classElement.getAnnotation(DefaultContentUri.class);
                    final String contentUriPathFromAnnotation;
                    final String contentUriAuthorityFromAnnotation;
                    if (defaultContentUriAnnotation != null) {
                        contentUriPathFromAnnotation = defaultContentUriAnnotation.path();
                        contentUriAuthorityFromAnnotation = defaultContentUriAnnotation.authority();
                    } else {
                        contentUriPathFromAnnotation = "";
                        contentUriAuthorityFromAnnotation = "";
                    }

                    final String contentUriPath;
                    if (contentUriPathFromAnnotation.isEmpty()) {
                        final DatabaseTable databaseTable = classElement.getAnnotation(DatabaseTable.class);
                        if (databaseTable == null || databaseTable.tableName().isEmpty()) {
                            contentUriPath = classElement.getSimpleName().toString().toLowerCase();
                        } else {
                            contentUriPath = databaseTable.tableName().toLowerCase();
                        }
                    } else {
                        contentUriPath = contentUriPathFromAnnotation;
                    }

                    final DefaultContentMimeTypeVnd defaultContentMimeTypeVndAnnotation = classElement.getAnnotation(DefaultContentMimeTypeVnd.class);
                    final String mimeTypeVndNameFromAnnotation;
                    final String mimeTypeVndTypeFromAnnotation;
                    if (defaultContentMimeTypeVndAnnotation != null) {
                        mimeTypeVndNameFromAnnotation = defaultContentMimeTypeVndAnnotation.name();
                        mimeTypeVndTypeFromAnnotation = defaultContentMimeTypeVndAnnotation.type();
                    } else {
                        mimeTypeVndNameFromAnnotation = "";
                        mimeTypeVndTypeFromAnnotation = "";
                    }

                    final String mimeTypeVndType;
                    if (mimeTypeVndTypeFromAnnotation.isEmpty()) {
                        final DatabaseTable databaseTable = classElement.getAnnotation(DatabaseTable.class);
                        if (databaseTable == null || databaseTable.tableName().isEmpty()) {
                            mimeTypeVndType = classElement.getSimpleName().toString().toLowerCase();
                        } else {
                            mimeTypeVndType = databaseTable.tableName().toLowerCase();
                        }
                    } else {
                        mimeTypeVndType = contentUriPathFromAnnotation;
                    }

                    final String contractClassName = String.format("%sContract", classElement.getSimpleName().toString());
                    writer.beginType(contractClassName, "class", EnumSet.of(STATIC, PUBLIC, FINAL), null, "BaseColumns")
                            .emitField("String", "CONTENT_URI_PATH", EnumSet.of(STATIC, PUBLIC, FINAL), JavaWriter.stringLiteral(contentUriPath))
                            .emitField("String", "AUTHORITY", EnumSet.of(STATIC, PUBLIC, FINAL), contentUriAuthorityFromAnnotation == null || contentUriAuthorityFromAnnotation.isEmpty() ? SUPER_AUTHORITY : JavaWriter.stringLiteral(contentUriAuthorityFromAnnotation))
                            .emitEmptyLine()
                            .emitField("String", "MIMETYPE_TYPE", EnumSet.of(STATIC, PUBLIC, FINAL), JavaWriter.stringLiteral(mimeTypeVndType))
                            .emitField("String", "MIMETYPE_NAME", EnumSet.of(STATIC, PUBLIC, FINAL), mimeTypeVndNameFromAnnotation == null || mimeTypeVndNameFromAnnotation.isEmpty() ? SUPER_MIME_TYPE_NAME : JavaWriter.stringLiteral(mimeTypeVndNameFromAnnotation))
                            .emitEmptyLine()
                            .emitField("int", "CONTENT_URI_PATTERN_MANY", EnumSet.of(STATIC, PUBLIC, FINAL), String.valueOf(contractCount * COUNT_FACTOR + patternCode++))
                            .emitField("int", "CONTENT_URI_PATTERN_ONE", EnumSet.of(STATIC, PUBLIC, FINAL), String.valueOf(contractCount * COUNT_FACTOR + patternCode++))
                            .emitEmptyLine()
                            .emitField("Uri", "CONTENT_URI", EnumSet.of(STATIC, PUBLIC, FINAL), DEFAULT_CONTENT_URI_STATEMENT)
                            .emitEmptyLine()
                            .beginMethod(null, contractClassName, EnumSet.of(PRIVATE))
                            .endMethod()
                            .emitEmptyLine();

                    final List<Element> fields = getAllElementsAnnotatedWith(DatabaseField.class, classElement);
                    for (final Element field : fields) {
                        final String fieldName = field.getSimpleName().toString();
                        if (!("_id".equals(fieldName) || "_id".equals(field.getAnnotation(DatabaseField.class).columnName()))) {
                            writer.emitField("String", fieldName.toUpperCase(), EnumSet.of(STATIC, PUBLIC, FINAL), JavaWriter.stringLiteral(fieldName));
                        }
                    }
                    writer.endType();
                    if (iterator.hasNext()) {
                        writer.emitEmptyLine();
                    }
                    contractCount++;
                }
                writer.endType();
                writer.emitEmptyLine();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                error(classElements, "can't open java file " + targetClassName);
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return true;
    }

    private Map<String, Set<Element>> groupElementsByContractClassName(final Collection<? extends Element> classElements) {
        final Map<String, Set<Element>> grouped = new HashMap<String, Set<Element>>(classElements.size());
        for (final Element classElement : classElements) {
            final SuperContract contractAnnotation = classElement.getAnnotation(SuperContract.class);
            final String contractClassName = contractAnnotation.contractClassName();

            final String targetClassName;
            if (contractClassName == null || contractClassName.isEmpty()) {
                final PackageElement targetPackage;
                targetPackage = CodeGen.getPackage(classElement);
                targetClassName = targetPackage.getQualifiedName().toString() + '.' + classElement.getSimpleName() + "Contract";
            } else {
                targetClassName = contractClassName;
            }

            if (grouped.containsKey(targetClassName)) {
                grouped.get(targetClassName).add(classElement);
            } else {
                final Set<Element> elements = new TreeSet<Element>(this);
                elements.add(classElement);
                grouped.put(targetClassName, elements);
            }
        }
        return grouped;
    }

    private static List<Element> getAllElementsAnnotatedWith(final Class<? extends Annotation> class1, final Element classElement) {
        final List<Element> allFields = getEnclosedElements(classElement, ElementKind.FIELD);
        final List<Element> result = new ArrayList<Element>(allFields.size());
        for (final Element fieldElement : allFields) {
            if (fieldElement.getAnnotation(class1) != null) {
                result.add(fieldElement);
            }
        }
        return result;
    }

    private void error(final Iterable<Element> elements, final CharSequence message) {
        for (final Element element : elements) {
            processingEnv.getMessager().printMessage(Kind.ERROR, message, element);
        }
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

    @Override
    public int compare(final Element o1, final Element o2) {
        final String className1 = o1.getSimpleName().toString();
        final String className2 = o2.getSimpleName().toString();
        return className1.compareTo(className2);
    }
}
