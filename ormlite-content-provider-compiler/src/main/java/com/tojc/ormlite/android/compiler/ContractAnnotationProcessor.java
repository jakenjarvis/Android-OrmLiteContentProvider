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
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.squareup.java.JavaWriter;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentMimeTypeVnd;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentUri;

/**
 * Annotation processor
 * @author <a href=\"mailto:christoffer@christoffer.me\">Christoffer Pettersson</a>
 */

@SupportedAnnotationTypes("com.j256.ormlite.table.DatabaseTable")
public class ContractAnnotationProcessor extends AbstractProcessor {

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnvironment) {

        // Create a string builder for out debug messages
        StringBuilder debug = new StringBuilder();

        // Get all classes that has the annotation
        Set<? extends Element> classElements = roundEnvironment.getElementsAnnotatedWith(DatabaseTable.class);

        // For each class that has the annotation
        for (final Element classElement : classElements) {

            // Get the annotation information
            DatabaseTable databaseTableAnnotation = classElement.getAnnotation(DatabaseTable.class);
            String dataBaseTableName = databaseTableAnnotation.tableName();

            DefaultContentUri defaultContentUriAnnotation = classElement.getAnnotation(DefaultContentUri.class);
            String contentUriAuthority = defaultContentUriAnnotation.authority();
            String contentUriPath = defaultContentUriAnnotation.path();

            DefaultContentMimeTypeVnd defaultContentMimeTypeVnd = classElement.getAnnotation(DefaultContentMimeTypeVnd.class);
            String mimeTypeVndName = defaultContentMimeTypeVnd.name();
            String mimeTypeVndType = defaultContentMimeTypeVnd.type();

            // Add some debug information
            debug.append("annotation-table-name:" + dataBaseTableName + "\n");

            PackageElement targetPackage = CodeGen.getPackage(classElement);
            String targetClassName = targetPackage.getQualifiedName().toString() + "." + classElement.getSimpleName() + "Contract";
            System.out.println("filename " + targetClassName);
            Writer out = null;
            try {
                JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(targetClassName, (Element[]) null);
                System.out.println("sourceFile " + sourceFile.toUri().toString());

                out = sourceFile.openWriter();
                JavaWriter writer = new JavaWriter(out);
                writer.emitPackage(targetPackage.getQualifiedName().toString())//
                        .emitEmptyLine()//
                        .emitImports("android.net.Uri")//
                        .emitImports("android.content.ContentResolver")//
                        .emitImports("android.provider.BaseColumns")//
                        .emitEmptyLine();//

                // public static final String TABLENAME = "curren_weather";

                String defaultContentUriStatement = "new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(AUTHORITY).appendPath(CONTENT_URI_PATH).build()";

                writer.beginType(targetClassName, "class", Modifier.PUBLIC | Modifier.FINAL, null, "BaseColumns") //
                        .emitField("String", "AUTHORITY", Modifier.STATIC | Modifier.PUBLIC | Modifier.FINAL, JavaWriter.stringLiteral(contentUriAuthority))//
                        .emitField("String", "TABLENAME", Modifier.STATIC | Modifier.PUBLIC | Modifier.FINAL, JavaWriter.stringLiteral(dataBaseTableName))//
                        .emitEmptyLine()//
                        .emitField("String", "CONTENT_URI_PATH", Modifier.STATIC | Modifier.PUBLIC | Modifier.FINAL, JavaWriter.stringLiteral(contentUriPath))//
                        .emitEmptyLine()//
                        .emitField("String", "MIMETYPE_TYPE", Modifier.STATIC | Modifier.PUBLIC | Modifier.FINAL, JavaWriter.stringLiteral(mimeTypeVndType))//
                        .emitField("String", "MIMETYPE_NAME", Modifier.STATIC | Modifier.PUBLIC | Modifier.FINAL, JavaWriter.stringLiteral(mimeTypeVndName))//
                        .emitEmptyLine()//
                        .emitField("int", "CONTENT_URI_PATTERN_MANY", Modifier.STATIC | Modifier.PUBLIC | Modifier.FINAL, "1")//
                        .emitField("int", "CONTENT_URI_PATTERN_ONE", Modifier.STATIC | Modifier.PUBLIC | Modifier.FINAL, "2")//
                        .emitEmptyLine()//
                        .emitField("Uri", "CONTENT_URI", Modifier.STATIC | Modifier.PUBLIC | Modifier.FINAL, defaultContentUriStatement)//
                        .emitEmptyLine()//
                        .emitEmptyLine();

                List<Element> fields = getAllElementsAnnotatedWith(debug, DatabaseField.class, classElement);
                for (Element field : fields) {
                    String fieldName = field.getSimpleName().toString();
                    if (!("_id".equals(fieldName) || "_id".equals(field.getAnnotation(DatabaseField.class).columnName()))) {
                        writer.emitField("String", fieldName.toUpperCase(), Modifier.STATIC | Modifier.PUBLIC | Modifier.FINAL, JavaWriter.stringLiteral(fieldName))//
                                .emitEmptyLine();//
                    }
                }
                writer.endType();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                error(classElement, debug, "can't open java file " + targetClassName);
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {

                    }
                }
            }
        }

        System.out.println(debug.toString());
        return true;

    }

    private List<Element> getAllElementsAnnotatedWith(final StringBuilder debug, Class<? extends Annotation> class1, Element classElement) {
        debug.append("annotorm" + "\n");
        List<Element> allFields = getEnclosedElements(debug, classElement, ElementKind.FIELD);
        List<Element> result = new ArrayList<Element>();
        for (final Element fieldElement : allFields) {
            if (fieldElement.getAnnotation(class1) != null) {
                debug.append("annotorm good" + fieldElement + " " + fieldElement.toString() + "\n");
                result.add(fieldElement);
            }
        }
        return result;
    }

    private void error(final Element element, final StringBuilder debug, final String message) {

        // Debug output (uncomment this to show debug message)
        // processingEnv.getMessager().printMessage(Kind.ERROR, debug.toString() + "\n\n" + message,
        // element);

        // Production output (uncomment this for production message)
        processingEnv.getMessager().printMessage(Kind.ERROR, message, element);

    }

    private List<Element> getEnclosedElements(final StringBuilder debug, final Element element, final ElementKind elementKind) {

        List<Element> list = new ArrayList<Element>();

        for (Element enclosedElement : element.getEnclosedElements()) {
            debug.append("enclosed:" + enclosedElement.getSimpleName() + ":" + enclosedElement.getKind() + "\n");
            if (enclosedElement.getKind() == elementKind) {
                list.add(enclosedElement);
            }
        }

        return list;

    }

}
