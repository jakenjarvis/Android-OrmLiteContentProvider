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
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.Contract;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentMimeTypeVnd;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentUri;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;
import java.io.IOException;
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
 * Contract annotation processor
 *
 * @author <a href=\"mailto:christoffer@christoffer.me\">Christoffer Pettersson</a>
 * @author Michael Cramer
 * @see Contract
 * @since 1.0.4
 */
@SupportedAnnotationTypes("com.tojc.ormlite.android.annotation.AdditionalAnnotation.Contract")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ContractAnnotationProcessor extends AbstractProcessor {
    private static final String DEFAULT_CONTENT_URI_STATEMENT = "new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(AUTHORITY).appendPath(CONTENT_URI_PATH).build()";
    private static final String SUPER_MIME_TYPE_NAME = "CONTRACT_MIME_TYPE_NAME";
    private static final String SUPER_AUTHORITY = "CONTRACT_AUTHORITY";
    private static final String EMPTY_LITERAL = JavaWriter.stringLiteral("");
    private int patternCode = 1;

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnvironment) {
        // Get all classes that has the annotation
        final Set<? extends Element> annotatedElements = roundEnvironment.getElementsAnnotatedWith(Contract.class);

        // Get the annotation information grouped by ContractClassName
        final Map<String, Set<Element>> grouped = groupElementsByContractClassName(annotatedElements);

        // process every ContractClassName
        for (final Map.Entry<String, Set<Element>> groupedElements : grouped.entrySet()) {
            final String targetClassName = groupedElements.getKey();
            final Set<Element> classElements = groupedElements.getValue();

            final String targetPackageName = targetClassName.substring(0, targetClassName.lastIndexOf('.'));

            final boolean multipleClassesForContract = classElements.size() > 1;

            Writer out = null;
            try {
                final JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(targetClassName, (Element[]) null);

                out = sourceFile.openWriter();
                final JavaWriter writer = new JavaWriter(out);

                // package and import statements
                writer.emitPackage(targetPackageName)
                        .emitImports(android.net.Uri.class.getCanonicalName())
                        .emitImports(android.content.ContentResolver.class.getCanonicalName())
                        .emitImports(android.provider.BaseColumns.class.getCanonicalName())
                        .emitEmptyLine();

                if (multipleClassesForContract) {
                    // super contract class with private constructor
                    writer.beginType(targetClassName, "class", EnumSet.of(PUBLIC, FINAL))
                            .emitField("String", SUPER_AUTHORITY, EnumSet.of(STATIC, PRIVATE, FINAL), JavaWriter.stringLiteral(targetPackageName))
                            .emitField("String", SUPER_MIME_TYPE_NAME, EnumSet.of(STATIC, PRIVATE, FINAL), JavaWriter.stringLiteral(targetPackageName + ".provider"))
                            .emitEmptyLine()
                            .beginMethod(null, targetClassName, EnumSet.of(PRIVATE))
                            .endMethod()
                            .emitEmptyLine();
                }

                // For each class that has the annotation
                final Iterator<Element> iterator = classElements.iterator();
                while (iterator.hasNext()) {
                    final Element classElement = iterator.next();

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
                        mimeTypeVndType = mimeTypeVndTypeFromAnnotation;
                    }

                    final String contractClassName = multipleClassesForContract ? String.format("%sContract", classElement.getSimpleName().toString()) : targetClassName.substring(targetClassName.lastIndexOf('.') + 1, targetClassName.length());
                    final EnumSet<Modifier> classModifiers = multipleClassesForContract ? EnumSet.of(STATIC, PUBLIC, FINAL) : EnumSet.of(PUBLIC, FINAL);

                    final String authority = multipleClassesForContract && (contentUriAuthorityFromAnnotation == null || contentUriAuthorityFromAnnotation.isEmpty()) ? SUPER_AUTHORITY : JavaWriter.stringLiteral(contentUriAuthorityFromAnnotation);
                    final String vndName = multipleClassesForContract && (mimeTypeVndNameFromAnnotation == null || mimeTypeVndNameFromAnnotation.isEmpty()) ? SUPER_MIME_TYPE_NAME : JavaWriter.stringLiteral(mimeTypeVndNameFromAnnotation);

                    writer.beginType(contractClassName, "class", classModifiers, null, "BaseColumns")
                            .emitField("String", "CONTENT_URI_PATH", EnumSet.of(STATIC, PUBLIC, FINAL), JavaWriter.stringLiteral(contentUriPath))
                            .emitField("String", "AUTHORITY", EnumSet.of(STATIC, PUBLIC, FINAL), authority.isEmpty() || EMPTY_LITERAL.equals(authority) ? JavaWriter.stringLiteral(targetPackageName) : authority)
                            .emitEmptyLine()
                            .emitField("String", "MIMETYPE_TYPE", EnumSet.of(STATIC, PUBLIC, FINAL), JavaWriter.stringLiteral(mimeTypeVndType))
                            .emitField("String", "MIMETYPE_NAME", EnumSet.of(STATIC, PUBLIC, FINAL), vndName.isEmpty() || EMPTY_LITERAL.equals(vndName) ? JavaWriter.stringLiteral(String.format("%s.provider", targetPackageName)) : vndName)
                            .emitEmptyLine()
                            .emitField("int", "CONTENT_URI_PATTERN_MANY", EnumSet.of(STATIC, PUBLIC, FINAL), String.valueOf(patternCode++))
                            .emitField("int", "CONTENT_URI_PATTERN_ONE", EnumSet.of(STATIC, PUBLIC, FINAL), String.valueOf(patternCode++))
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
                }
                if (multipleClassesForContract) {
                    writer.endType();
                }
                writer.emitEmptyLine();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                for (final Element element : classElements) {
                    processingEnv.getMessager().printMessage(Kind.ERROR, "can't open java file " + targetClassName, element);
                }
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

    public PackageElement getPackage(Element type) {
        while (type.getKind() != ElementKind.PACKAGE) {
            type = type.getEnclosingElement();
        }
        return (PackageElement) type;
    }

    public List<Element> getAllElementsAnnotatedWith(final Class<? extends Annotation> class1, final Element classElement) {
        final List<Element> allFields = getEnclosedElements(classElement, ElementKind.FIELD);
        final List<Element> result = new ArrayList<Element>(allFields.size());
        for (final Element fieldElement : allFields) {
            if (fieldElement.getAnnotation(class1) != null) {
                result.add(fieldElement);
            }
        }
        return result;
    }

    public List<Element> getEnclosedElements(final Element element, final ElementKind elementKind) {
        final List<? extends Element> enclosedElements = element.getEnclosedElements();
        final List<Element> list = new ArrayList<Element>(enclosedElements.size());
        for (final Element enclosedElement : enclosedElements) {
            if (enclosedElement.getKind() == elementKind) {
                list.add(enclosedElement);
            }
        }
        return list;
    }

    public Map<String, Set<Element>> groupElementsByContractClassName(final Collection<? extends Element> classElements) {
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
