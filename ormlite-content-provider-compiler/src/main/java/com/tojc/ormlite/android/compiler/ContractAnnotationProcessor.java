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
import com.tojc.ormlite.android.cursor.EntityUtils;

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
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
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

    private static final String CURSOR_COLUMN_TYPE_INTEGER = EntityUtils.class.getCanonicalName() + ".FIELD_TYPE_INTEGER";
    private static final String CURSOR_COLUMN_TYPE_FLOAT = EntityUtils.class.getCanonicalName() + ".FIELD_TYPE_FLOAT";
    private static final String CURSOR_COLUMN_TYPE_STRING = EntityUtils.class.getCanonicalName() + ".FIELD_TYPE_STRING";
    private static final String CURSOR_COLUMN_TYPE_DOUBLE = EntityUtils.class.getCanonicalName() + ".FIELD_TYPE_DOUBLE";

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

        final StringBuilder sbInitialColumnsValue = new StringBuilder();
        final StringBuilder sbInitialTypesValue = new StringBuilder();
        boolean isEmittedField = false;
        final List<String> constantNames = new ArrayList<String>();
        final List<Element> fields = getAllElementsAnnotatedWith(DatabaseField.class, classElement);
        for (final Element field : fields) {
            final String columnName = getColumnName(field);
            final String constantName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, columnName);
            final String cursorColumnTypeName = getCursorColumnTypeName(field);

            if (!("_id".equals(columnName))) {
                writer.emitField("String", constantName, EnumSet.of(STATIC, PUBLIC, FINAL), JavaWriter.stringLiteral(columnName));
                if (sbInitialColumnsValue.length() > 0) {
                    sbInitialColumnsValue.append(",\n");
                }
                sbInitialColumnsValue.append(constantName);

                if (sbInitialTypesValue.length() > 0) {
                    sbInitialTypesValue.append(",\n");
                }
                sbInitialTypesValue.append(cursorColumnTypeName);

                isEmittedField = true;
            } else {
                sbInitialColumnsValue.append("BaseColumns._ID");
                sbInitialTypesValue.append(CURSOR_COLUMN_TYPE_INTEGER);
            }

            constantNames.add(constantName);
        }

        if (constantNames.size() > 0) {
            if (isEmittedField) {
                writer.emitEmptyLine();
            }
            writer.emitField("String[]", "ALL_COLUMNS", EnumSet.of(STATIC, PUBLIC, FINAL), "{\n" + sbInitialColumnsValue.toString() + "\n}");

            writer.emitEmptyLine();

            writer.emitField("int[]", "ALL_COLUMN_TYPES", EnumSet.of(STATIC, PUBLIC, FINAL), "{\n" + sbInitialTypesValue.toString() + "\n}");

            writer.emitEmptyLine();

            writer.beginType("ColumnIndices", "interface", EnumSet.of(PUBLIC));
            for (int i = 0; i < constantNames.size(); i++) {
                final String constantName = constantNames.get(i);
                final String indexName = "COL" + (constantName.startsWith("_") ? "" : "_") + constantName;
                writer.emitField("int", indexName, EnumSet.noneOf(Modifier.class), String.valueOf(i));
            }
            writer.endType();

            writer.emitEmptyLine();

            writer.beginInitializer(true);
            Class<EntityUtils> entityUtilsClass = EntityUtils.class;
            String entityClassName = ((TypeElement) classElement).getQualifiedName().toString();
            writer.emitStatement("%s.registerContractInfo(MIMETYPE_TYPE, %s.class, ALL_COLUMNS, ALL_COLUMN_TYPES)", entityUtilsClass.getCanonicalName(), entityClassName);
            writer.endInitializer();
        }

        writer.endType();
    }

    private String getCursorColumnTypeName(Element field) {
        final TypeMirror typeMirror = field.asType();
        final TypeKind kind = typeMirror.getKind();
        switch (kind) {
            case INT:
                return CURSOR_COLUMN_TYPE_INTEGER;
            case FLOAT:
                return CURSOR_COLUMN_TYPE_FLOAT;
            case DOUBLE:
                return CURSOR_COLUMN_TYPE_DOUBLE;
            case DECLARED:
                Element typeElem = processingEnv.getTypeUtils().asElement(typeMirror);
                if (typeElem instanceof TypeElement) {
                    String className = ((TypeElement) typeElem).getQualifiedName().toString();
                    if (className.equals("java.lang.Integer")) {
                        return CURSOR_COLUMN_TYPE_INTEGER;
                    } else if (className.equals("java.lang.Float")) {
                        return CURSOR_COLUMN_TYPE_FLOAT;
                    } else if (className.equals("java.lang.Double")) {
                        return CURSOR_COLUMN_TYPE_DOUBLE;
                    }
                }
                return CURSOR_COLUMN_TYPE_STRING;
            default:
                return CURSOR_COLUMN_TYPE_STRING;
        }
    }

    private String getColumnName(Element field) {
        final String fieldName = field.getSimpleName().toString();
        final DatabaseField databaseFieldAnnotation = field.getAnnotation(DatabaseField.class);
        final String annotatedColumnName = databaseFieldAnnotation.columnName();
        return annotatedColumnName != null && annotatedColumnName.length() > 0 ? annotatedColumnName : fieldName;
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
