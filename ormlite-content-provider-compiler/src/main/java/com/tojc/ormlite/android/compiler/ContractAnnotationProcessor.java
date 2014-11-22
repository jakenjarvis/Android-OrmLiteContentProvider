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
package com.tojc.ormlite.android.compiler;

import com.google.common.base.CaseFormat;
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
 * @author Stéphane NICOLAS
 * @author Michael Cramer
 * @author Jaken
 * @see Contract
 * @since 1.0.2
 */
@SupportedAnnotationTypes("com.tojc.ormlite.android.annotation.AdditionalAnnotation.Contract")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ContractAnnotationProcessor extends AbstractProcessor {
    private static final String DEFAULT_CONTENT_URI_STATEMENT = "new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(AUTHORITY).appendPath(CONTENT_URI_PATH).build()";
    private static final String CONTRACT_MIME_TYPE_NAME = "CONTRACT_MIME_TYPE_NAME";
    private static final String CONTRACT_AUTHORITY = "CONTRACT_AUTHORITY";
    private static final String CONTRACT_CLASS_SUFFIX = "Contract";
    private static final String MIMETYPE_NAME_SUFFIX = "provider";
    private int patternCode = 1;

    // http://stackoverflow.com/questions/8185331/forward-compatible-java-6-annotation-processor-and-supportedsourceversion
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnvironment) {
        // Get all classes that has the annotation
        final Set<? extends Element> annotatedElements = roundEnvironment.getElementsAnnotatedWith(Contract.class);

        // Get the annotation information grouped by ContractClassName
        final Map<String, Set<Element>> grouped = getGroupElementsByContractClassName(annotatedElements);

        // process every ContractClassName
        for (final Map.Entry<String, Set<Element>> groupedElements : grouped.entrySet()) {
            final String targetClassName = groupedElements.getKey();
            final Set<Element> classElements = groupedElements.getValue();

            final boolean multipleClassesForContract = classElements.size() > 1;

            final String targetPackageName = targetClassName.substring(0, targetClassName.lastIndexOf('.'));

            Writer out = null;
            try {
                final JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(targetClassName, (Element[]) null);

                out = sourceFile.openWriter();
                final JavaWriter writer = new JavaWriter(out);

                // package and import statements
                writer.emitPackage(targetPackageName)
                        //.emitImports(android.net.Uri.class.getCanonicalName())
                        //.emitImports(android.content.ContentResolver.class.getCanonicalName())
                        //.emitImports(android.provider.BaseColumns.class.getCanonicalName())
                        .emitImports("android.net.Uri")
                        .emitImports("android.content.ContentResolver")
                        .emitImports("android.provider.BaseColumns")
                        .emitEmptyLine();

                final String defaultAuthority = targetPackageName;
                final String defaultMimeTypeName = targetPackageName + "." + MIMETYPE_NAME_SUFFIX;

                if (multipleClassesForContract) {
                    // contract class with private constructor
                    writer.beginType(targetClassName, "class", EnumSet.of(PUBLIC, FINAL))
                            .emitField("String", CONTRACT_AUTHORITY, EnumSet.of(STATIC, PRIVATE, FINAL), JavaWriter.stringLiteral(defaultAuthority))
                            .emitField("String", CONTRACT_MIME_TYPE_NAME, EnumSet.of(STATIC, PRIVATE, FINAL), JavaWriter.stringLiteral(defaultMimeTypeName))
                            .emitEmptyLine()
                            .beginMethod(null, targetClassName, EnumSet.of(PRIVATE))
                            .endMethod()
                            .emitEmptyLine();
                }

                // For each class that has the annotation
                final Iterator<Element> iterator = classElements.iterator();
                while (iterator.hasNext()) {
                    final Element classElement = iterator.next();

                    writeContractClass(
                        writer,
                        classElement,
                        multipleClassesForContract,
                        targetClassName,
                        defaultAuthority,
                        defaultMimeTypeName);

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

    private void writeContractClass(
        JavaWriter writer,
        Element classElement,
        boolean multipleClassesForContract,
        String targetClassName,
        String defaultAuthority,
        String defaultMimeTypeName
    ) throws IOException {

        // --------------------------------------------------
        // TABLE_NAME
        // --------------------------------------------------
        String writer_table_name = "";
        String databaseTableName = "";
        final DatabaseTable databaseTable = classElement.getAnnotation(DatabaseTable.class);
        if (databaseTable != null) {
            databaseTableName = databaseTable.tableName();
        }
        if (databaseTableName == null || databaseTableName.length() == 0) {
            databaseTableName = classElement.getSimpleName().toString();
        }
        writer_table_name = JavaWriter.stringLiteral(databaseTableName);

        // --------------------------------------------------
        // contractClassName, classModifiers
        // --------------------------------------------------
        String contractClassName = "";
        EnumSet<Modifier> classModifiers = null;
        if (multipleClassesForContract) {
            contractClassName = classElement.getSimpleName().toString(); // + CONTRACT_CLASS_SUFFIX;
            classModifiers = EnumSet.of(STATIC, PUBLIC, FINAL);
        } else {
            contractClassName = targetClassName.substring(targetClassName.lastIndexOf('.') + 1, targetClassName.length());
            classModifiers = EnumSet.of(PUBLIC, FINAL);
        }

        // --------------------------------------------------
        // CONTENT_URI_PATH
        // --------------------------------------------------
        String writer_content_uri_path = "";
        final DefaultContentUri defaultContentUriAnnotation = classElement.getAnnotation(DefaultContentUri.class);
        String contentUriPath = "";
        String contentUriAuthority = "";
        if (defaultContentUriAnnotation != null) {
            contentUriPath = defaultContentUriAnnotation.path();
            contentUriAuthority = defaultContentUriAnnotation.authority();
        } else {
            contentUriPath = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, databaseTableName);
            contentUriAuthority = defaultAuthority;
        }
        writer_content_uri_path = JavaWriter.stringLiteral(contentUriPath);

        // --------------------------------------------------
        // AUTHORITY
        // --------------------------------------------------
        String writer_authority = "";
        if (multipleClassesForContract) {
            if (contentUriAuthority == null || contentUriAuthority.length() == 0) {
                writer_authority = CONTRACT_AUTHORITY;
            } else if (defaultAuthority.equals(contentUriAuthority)) {
                writer_authority = CONTRACT_AUTHORITY;
            } else {
                writer_authority = JavaWriter.stringLiteral(contentUriAuthority);
            }
        } else {
            if (contentUriAuthority == null || contentUriAuthority.length() == 0) {
                writer_authority = JavaWriter.stringLiteral(defaultAuthority);
            } else {
                writer_authority = JavaWriter.stringLiteral(contentUriAuthority);
            }
        }

        // --------------------------------------------------
        // MIMETYPE_TYPE
        // --------------------------------------------------
        String writer_mimetype_type = "";
        final DefaultContentMimeTypeVnd defaultContentMimeTypeVndAnnotation = classElement.getAnnotation(DefaultContentMimeTypeVnd.class);
        String mimeTypeVndName = "";
        String mimeTypeVndType = "";
        if (defaultContentMimeTypeVndAnnotation != null) {
            mimeTypeVndName = defaultContentMimeTypeVndAnnotation.name();
            mimeTypeVndType = defaultContentMimeTypeVndAnnotation.type();
        }
        if (mimeTypeVndName == null || mimeTypeVndName.length() == 0) {
            mimeTypeVndName = defaultMimeTypeName;
        }
        if (mimeTypeVndType == null || mimeTypeVndType.length() == 0) {
            mimeTypeVndType = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, databaseTableName);
        }
        writer_mimetype_type = JavaWriter.stringLiteral(mimeTypeVndType);

        // --------------------------------------------------
        // MIMETYPE_NAME
        // --------------------------------------------------
        String writer_mimetype_name = "";
        if (multipleClassesForContract) {
            if (mimeTypeVndName == null || mimeTypeVndName.length() == 0) {
                writer_mimetype_name = CONTRACT_MIME_TYPE_NAME;
            } else if (defaultMimeTypeName.equals(mimeTypeVndName)) {
                writer_mimetype_name = CONTRACT_MIME_TYPE_NAME;
            } else {
                writer_mimetype_name = JavaWriter.stringLiteral(mimeTypeVndName);
            }
        } else {
            if (mimeTypeVndName == null || mimeTypeVndName.length() == 0) {
                writer_mimetype_name = JavaWriter.stringLiteral(defaultMimeTypeName);
            } else {
                writer_mimetype_name = JavaWriter.stringLiteral(mimeTypeVndName);
            }
        }

        // --------------------------------------------------
        // Create Contract Class
        // --------------------------------------------------
        writer.beginType(contractClassName, "class", classModifiers, null, "BaseColumns")
                .emitField("String", "TABLE_NAME", EnumSet.of(STATIC, PUBLIC, FINAL), writer_table_name)
                .emitEmptyLine()
                .emitField("String", "CONTENT_URI_PATH", EnumSet.of(STATIC, PUBLIC, FINAL), writer_content_uri_path)
                .emitField("String", "AUTHORITY", EnumSet.of(STATIC, PUBLIC, FINAL), writer_authority)
                .emitEmptyLine()
                .emitField("String", "MIMETYPE_TYPE", EnumSet.of(STATIC, PUBLIC, FINAL), writer_mimetype_type)
                .emitField("String", "MIMETYPE_NAME", EnumSet.of(STATIC, PUBLIC, FINAL), writer_mimetype_name)
                .emitEmptyLine()
                .emitField("int", "CONTENT_URI_PATTERN_MANY", EnumSet.of(STATIC, PUBLIC, FINAL), String.valueOf(patternCode++))
                .emitField("int", "CONTENT_URI_PATTERN_ONE", EnumSet.of(STATIC, PUBLIC, FINAL), String.valueOf(patternCode++))
                .emitEmptyLine()
                .emitField("Uri", "CONTENT_URI", EnumSet.of(STATIC, PUBLIC, FINAL), DEFAULT_CONTENT_URI_STATEMENT)
                .emitEmptyLine()
                .beginMethod(null, contractClassName, EnumSet.of(PRIVATE))
                .endMethod()
                .emitEmptyLine();

        final StringBuilder sbInitialValue = new StringBuilder();
        boolean isEmittedField = false;
        sbInitialValue.append("{\nBaseColumns._ID");
        final List<Element> fields = getAllElementsAnnotatedWith(DatabaseField.class, classElement);
        for (final Element field : fields) {
            final String fieldName = field.getSimpleName().toString();
            final DatabaseField databaseFieldAnnotation = field.getAnnotation(DatabaseField.class);
            final String annotatedColumnName = databaseFieldAnnotation.columnName();
            if (!("_id".equals(fieldName) || "_id".equals(annotatedColumnName))) {
                final String columnName = annotatedColumnName != null && annotatedColumnName.length() > 0 ? annotatedColumnName : fieldName;
                final String constantName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, columnName);
                writer.emitField("String", constantName, EnumSet.of(STATIC, PUBLIC, FINAL), JavaWriter.stringLiteral(columnName));
                sbInitialValue.append(",\n").append(constantName);
                isEmittedField = true;
            }
        }
        sbInitialValue.append("\n}");

        if (isEmittedField) {
            writer.emitEmptyLine();
        }
        writer.emitField("String[]", "ALL_COLUMNS", EnumSet.of(STATIC, PUBLIC, FINAL), sbInitialValue.toString());

        writer.endType();
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

    public Map<String, Set<Element>> getGroupElementsByContractClassName(final Collection<? extends Element> classElements) {
        final Map<String, Set<Element>> result = new HashMap<String, Set<Element>>();

        for (final Element classElement : classElements) {
            final Contract contractAnnotation = classElement.getAnnotation(Contract.class);
            final String contractClassName = contractAnnotation.contractClassName();

            final String targetClassName;
            if (contractClassName == null || contractClassName.isEmpty()) {
                final PackageElement targetPackage = getPackage(classElement);
                targetClassName = targetPackage.getQualifiedName().toString() + '.' + classElement.getSimpleName() + CONTRACT_CLASS_SUFFIX;
            } else {
                targetClassName = contractClassName;
            }

            if (result.containsKey(targetClassName)) {
                result.get(targetClassName).add(classElement);
            } else {
                final Set<Element> elements = new TreeSet<Element>(ELEMENT_COMPARATOR);
                elements.add(classElement);
                result.put(targetClassName, elements);
            }
        }
        return result;
    }

}
