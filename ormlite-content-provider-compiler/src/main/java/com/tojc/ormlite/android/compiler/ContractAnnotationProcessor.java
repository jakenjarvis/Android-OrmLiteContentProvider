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

import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;
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

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.squareup.javawriter.JavaWriter;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.Contract;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentMimeTypeVnd;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentUri;

/**
 * Annotation processor
 * @author <a href=\"mailto:christoffer@christoffer.me\">Christoffer Pettersson</a>
 */

@SupportedAnnotationTypes("com.tojc.ormlite.android.annotation.AdditionalAnnotation.Contract")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ContractAnnotationProcessor extends AbstractProcessor {

    private int patternCode = 1;

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnvironment) {

        // Get all classes that has the annotation
        Set<? extends Element> classElements = roundEnvironment.getElementsAnnotatedWith(Contract.class);

        // For each class that has the annotation
        for (final Element classElement : classElements) {

            // Get the annotation information
            Contract contractAnnotation = classElement.getAnnotation(Contract.class);
            String contractClassName = contractAnnotation.contractClassName();

            String targetPackageName;
            String targetClassName;
            if (contractClassName == null || contractClassName.length() == 0) {
                PackageElement targetPackage;
                targetPackage = CodeGen.getPackage(classElement);
                targetPackageName = targetPackage.getQualifiedName().toString();
                targetClassName = targetPackage.getQualifiedName().toString() + "." + classElement.getSimpleName() + "Contract";
            } else {
                targetPackageName = contractClassName.substring(0, contractClassName.lastIndexOf('.'));
                targetClassName = contractClassName;
            }

            DatabaseTable databaseTable = classElement.getAnnotation(DatabaseTable.class);
            String databaseTableName = "";
            if (databaseTable != null) {
                databaseTableName = databaseTable.tableName();
            }
            if (databaseTableName == null || databaseTableName.length() == 0) {
                databaseTableName = classElement.getSimpleName().toString();
            }

            DefaultContentUri defaultContentUriAnnotation = classElement.getAnnotation(DefaultContentUri.class);
            String contentUriAuthority = "";
            String contentUriPath = "";
            if (defaultContentUriAnnotation != null) {
                contentUriAuthority = defaultContentUriAnnotation.authority();
                contentUriPath = defaultContentUriAnnotation.path();
            }
            if (contentUriAuthority == null || contentUriAuthority.length() == 0) {
                contentUriAuthority = targetPackageName;
            }
            if (contentUriPath == null || contentUriPath.length() == 0) {
                // TODO use DataBase annotation
                contentUriPath = classElement.getSimpleName().toString().toLowerCase();
            }

            DefaultContentMimeTypeVnd defaultContentMimeTypeVndAnnotation = classElement.getAnnotation(DefaultContentMimeTypeVnd.class);
            String mimeTypeVndName = "";
            String mimeTypeVndType = "";
            if (defaultContentMimeTypeVndAnnotation != null) {
                mimeTypeVndName = defaultContentMimeTypeVndAnnotation.name();
                mimeTypeVndType = defaultContentMimeTypeVndAnnotation.type();
            }
            if (mimeTypeVndName == null || mimeTypeVndName.length() == 0) {
                mimeTypeVndName = targetPackageName + ".provider";
            }
            if (mimeTypeVndType == null || mimeTypeVndType.length() == 0) {
                mimeTypeVndType = classElement.getSimpleName().toString().toLowerCase();
            }

            Writer out = null;
            try {
                JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(targetClassName, (Element[]) null);

                out = sourceFile.openWriter();
                JavaWriter writer = new JavaWriter(out);
                writer.emitPackage(targetPackageName)//
                        .emitEmptyLine()//
                        .emitImports("android.net.Uri")//
                        .emitImports("android.content.ContentResolver")//
                        .emitImports("android.provider.BaseColumns")//
                        .emitEmptyLine();

                // public static final String TABLENAME = "curren_weather";

                String defaultContentUriStatement = "new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(AUTHORITY).appendPath(CONTENT_URI_PATH).build()";

                writer.beginType(targetClassName, "class", EnumSet.of(PUBLIC, FINAL), null, "BaseColumns") //
                        .emitField("String", "TABLE_NAME", EnumSet.of(STATIC, PUBLIC, FINAL), JavaWriter.stringLiteral(databaseTableName))//
                        .emitEmptyLine()//
                        .emitField("String", "AUTHORITY", EnumSet.of(STATIC, PUBLIC, FINAL), JavaWriter.stringLiteral(contentUriAuthority))//
                        .emitEmptyLine()//
                        .emitField("String", "CONTENT_URI_PATH", EnumSet.of(STATIC, PUBLIC, FINAL), JavaWriter.stringLiteral(contentUriPath))//
                        .emitEmptyLine()//
                        .emitField("String", "MIMETYPE_TYPE", EnumSet.of(STATIC, PUBLIC, FINAL), JavaWriter.stringLiteral(mimeTypeVndType))//
                        .emitField("String", "MIMETYPE_NAME", EnumSet.of(STATIC, PUBLIC, FINAL), JavaWriter.stringLiteral(mimeTypeVndName))//
                        .emitEmptyLine()//
                        .emitField("int", "CONTENT_URI_PATTERN_MANY", EnumSet.of(STATIC, PUBLIC, FINAL), String.valueOf(patternCode++))//
                        .emitField("int", "CONTENT_URI_PATTERN_ONE", EnumSet.of(STATIC, PUBLIC, FINAL), String.valueOf(patternCode++))//
                        .emitEmptyLine()//
                        .emitField("Uri", "CONTENT_URI", EnumSet.of(STATIC, PUBLIC, FINAL), defaultContentUriStatement)//
                        .emitEmptyLine()//
                        .beginMethod(null, targetClassName, EnumSet.of(PRIVATE))//
                        .endMethod()//
                        .emitEmptyLine()//
                        .emitEmptyLine();

                List<Element> fields = getAllElementsAnnotatedWith(DatabaseField.class, classElement);
                for (Element field : fields) {
                    String fieldName = field.getSimpleName().toString();
                    if (!("_id".equals(fieldName) || "_id".equals(field.getAnnotation(DatabaseField.class).columnName()))) {
                        writer.emitField("String", fieldName.toUpperCase(), EnumSet.of(STATIC, PUBLIC, FINAL), JavaWriter.stringLiteral(fieldName));
                    }
                }
                writer.endType();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                error(classElement, "can't open java file " + targetClassName);
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

    private List<Element> getAllElementsAnnotatedWith(Class<? extends Annotation> class1, Element classElement) {
        List<Element> allFields = getEnclosedElements(classElement, ElementKind.FIELD);
        List<Element> result = new ArrayList<Element>();
        for (final Element fieldElement : allFields) {
            if (fieldElement.getAnnotation(class1) != null) {
                result.add(fieldElement);
            }
        }
        return result;
    }

    private void error(final Element element, final String message) {
        processingEnv.getMessager().printMessage(Kind.ERROR, message, element);
    }

    private List<Element> getEnclosedElements(final Element element, final ElementKind elementKind) {

        List<Element> list = new ArrayList<Element>();

        for (Element enclosedElement : element.getEnclosedElements()) {
            if (enclosedElement.getKind() == elementKind) {
                list.add(enclosedElement);
            }
        }

        return list;
    }
}
