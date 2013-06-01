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
/*
 * @(#)AbstractAnnotationProcessorTest.java     5 Jun 2009
 */
package com.tojc.ormlite.android.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.processing.Processor;
import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ClassUtils;

/**
 * A base test class for {@link Processor annotation processor} testing that attempts to compile
 * source test cases that can be found on the classpath. Mixed with
 * https://code.google.com/p/acris/source
 * /browse/sesam/branches/1.1.0/sesam-annotations-support/src/test
 * /java/sk/seges/sesam/core/pap/AnnotationTest.java?r=5769 <br>
 * https://code.google.com/p/aphillips/source/browse/at-composite/trunk/pom.xml <br>
 * http://blog.xebia.com/2009/07/21/testing-annotation-processors/ <br>
 * https://code.google.com/p/acris/wiki/AnnotationProcessing_Testing<br>
 * @author aphillips
 * @since 5 Jun 2009
 */
abstract class AbstractAnnotationProcessorTest {
    private static final String SOURCE_FILE_SUFFIX = ".java";
    private static final JavaCompiler COMPILER = ToolProvider.getSystemJavaCompiler();
    protected static final String OUTPUT_FILE_SUFFIX = ".output";
    protected static final String OUTPUT_DIRECTORY = "target/generated-test";

    protected enum CompilerOptions {
        GENERATED_SOURCES_DIRECTORY("-s <directory>", "<directory>", "Specify where to place generated source files"), GENERATED_CLASSES_DIRECTORY("-d <directory>", "<directory>",
                "Specify where to place generated class files");

        private String option;
        private String description;
        private String parameter;

        CompilerOptions(String option, String parameter, String description) {
            this.option = option;
            this.parameter = parameter;
            this.description = description;
        }

        public String getOption() {
            return option;
        }

        public String[] getOption(String parameterValue) {
            if (parameter != null) {

                String[] result = new String[2];

                int index = option.indexOf(parameter);
                result[0] = option.substring(0, index).trim();
                result[1] = parameterValue;
                return result;
            }

            return new String[] {getOption()};
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * @return the processor instances that should be tested
     */
    protected abstract Collection<Processor> getProcessors();

    /**
     * Attempts to compile the given compilation units using the Java Compiler API.
     * <p>
     * The compilation units and all their dependencies are expected to be on the classpath.
     * @param compilationUnits
     *            the classes to compile
     * @return the {@link Diagnostic diagnostics} returned by the compilation, as demonstrated in
     *         the documentation for {@link JavaCompiler}
     * @see #compileTestCase(String...)
     */
    protected List<Diagnostic<? extends JavaFileObject>> compileTestCase(Class<?>... compilationUnits) {
        assert compilationUnits != null;

        String[] compilationUnitPaths = new String[compilationUnits.length];

        for (int i = 0; i < compilationUnitPaths.length; i++) {
            assert compilationUnits[i] != null;
            compilationUnitPaths[i] = toResourcePath(compilationUnits[i]);
        }

        return compileTestCase(compilationUnitPaths);
    }

    private static String toResourcePath(Class<?> clazz) {
        return ClassUtils.convertClassNameToResourcePath(clazz.getName()) + SOURCE_FILE_SUFFIX;
    }

    /**
     * Attempts to compile the given compilation units using the Java Compiler API.
     * <p>
     * The compilation units and all their dependencies are expected to be on the classpath.
     * @param compilationUnitPaths
     *            the paths of the source files to compile, as would be expected by
     *            {@link ClassLoader#getResource(String)}
     * @return the {@link Diagnostic diagnostics} returned by the compilation, as demonstrated in
     *         the documentation for {@link JavaCompiler}
     * @see #compileTestCase(Class...)
     */
    protected List<Diagnostic<? extends JavaFileObject>> compileTestCase(String... compilationUnitPaths) {
        assert compilationUnitPaths != null;

        Collection<File> compilationUnits;

        try {
            compilationUnits = findClasspathFiles(compilationUnitPaths);
        } catch (IOException exception) {
            throw new IllegalArgumentException("Unable to resolve compilation units " + Arrays.toString(compilationUnitPaths) + " due to: " + exception.getMessage(), exception);
        }

        DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<JavaFileObject>();
        StandardJavaFileManager fileManager = COMPILER.getStandardFileManager(diagnosticCollector, null, null);

        /*
         * Call the compiler with the "-proc:only" option. The "class names" option (which could, in
         * principle, be used instead of compilation units for annotation processing) isn't useful
         * in this case because only annotations on the classes being compiled are accessible.
         * Information about the classes being compiled (such as what they are annotated with) is
         * *not* available via the RoundEnvironment. However, if these classes are annotations, they
         * certainly need to be validated.
         */
        CompilationTask task = COMPILER.getTask(null, fileManager, diagnosticCollector, mergeCompilerOptions(Arrays.asList("-proc:only")), null,
                fileManager.getJavaFileObjectsFromFiles(compilationUnits));
        task.setProcessors(getProcessors());
        task.call();

        try {
            fileManager.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return diagnosticCollector.getDiagnostics();
    }

    private static Collection<File> findClasspathFiles(String[] filenames) throws IOException {
        Collection<File> classpathFiles = new ArrayList<File>(filenames.length);

        for (String filename : filenames) {
            classpathFiles.add(new ClassPathResource(filename).getFile());
        }

        return classpathFiles;
    }

    /**
     * Asserts that the compilation produced no errors, i.e. no diagnostics of type
     * {@link Kind#ERROR}.
     * @param diagnostics
     *            the result of the compilation
     * @see #assertCompilationReturned(Kind, long, List)
     * @see #assertCompilationReturned(Kind[], long[], List)
     */
    protected static void assertCompilationSuccessful(List<Diagnostic<? extends JavaFileObject>> diagnostics) {
        assert diagnostics != null;

        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
            assertFalse("Expected no errors", diagnostic.getKind().equals(Kind.ERROR));
        }

    }

    /**
     * Asserts that the compilation produced results of the following {@link Kind Kinds} at the
     * given line numbers, where the <em>n</em>th kind is expected at the <em>n</em>th line number.
     * <p>
     * Does not check that these is the <em>only</em> diagnostic kinds returned!
     * @param expectedDiagnosticKinds
     *            the kinds of diagnostic expected
     * @param expectedLineNumber
     *            the line numbers at which the diagnostics are expected
     * @param diagnostics
     *            the result of the compilation
     * @see #assertCompilationSuccessful(List)
     * @see #assertCompilationReturned(Kind, long, List)
     */
    protected static void assertCompilationReturned(Kind[] expectedDiagnosticKinds, long[] expectedLineNumbers, List<Diagnostic<? extends JavaFileObject>> diagnostics) {
        assert expectedDiagnosticKinds != null && expectedLineNumbers != null && expectedDiagnosticKinds.length == expectedLineNumbers.length;

        for (int i = 0; i < expectedDiagnosticKinds.length; i++) {
            assertCompilationReturned(expectedDiagnosticKinds[i], expectedLineNumbers[i], diagnostics);
        }

    }

    /**
     * Asserts that the compilation produced a result of the following {@link Kind} at the given
     * line number.
     * <p>
     * Does not check that this is the <em>only</em> diagnostic kind returned!
     * @param expectedDiagnosticKind
     *            the kind of diagnostic expected
     * @param expectedLineNumber
     *            the line number at which the diagnostic is expected
     * @param diagnostics
     *            the result of the compilation
     * @see #assertCompilationSuccessful(List)
     * @see #assertCompilationReturned(Kind[], long[], List)
     */
    protected static void assertCompilationReturned(Kind expectedDiagnosticKind, long expectedLineNumber, List<Diagnostic<? extends JavaFileObject>> diagnostics) {
        assert expectedDiagnosticKind != null && diagnostics != null;
        boolean expectedDiagnosticFound = false;

        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {

            if (diagnostic.getKind().equals(expectedDiagnosticKind) && diagnostic.getLineNumber() == expectedLineNumber) {
                expectedDiagnosticFound = true;
            }

        }

        assertTrue("Expected a result of kind " + expectedDiagnosticKind + " at line " + expectedLineNumber, expectedDiagnosticFound);
    }

    private List<String> mergeCompilerOptions(List<String> options) {

        if (options == null) {
            return Arrays.asList(getCompilerOptions());
        }
        List<String> result = new ArrayList<String>();

        for (String option : options) {
            result.add(option);
        }

        for (String option : getCompilerOptions()) {
            result.add(option);
        }

        return result;
    }

    protected String[] getCompilerOptions() {
        return CompilerOptions.GENERATED_SOURCES_DIRECTORY.getOption(ensureOutputDirectory().getAbsolutePath());
    }

    protected File ensureOutputDirectory() {
        File file = new File(OUTPUT_DIRECTORY);
        if (!file.exists()) {
            file.mkdirs();
        }

        return file;
    }

    protected static void assertOutput(File expectedResult, File output) throws IOException {
        String[] expectedContent = getContents(expectedResult);
        String[] outputContent = getContents(output);
        assertEquals(expectedContent.length, outputContent.length);

        for (int i = 0; i < expectedContent.length; i++) {
            assertEquals(expectedContent[i].trim(), outputContent[i].trim());
        }
    }

    private static String[] getContents(File file) throws IOException {
        List<String> content = new ArrayList<String>();

        BufferedReader input = new BufferedReader(new FileReader(file));
        try {
            String line = null; // not declared within while loop
            while ((line = input.readLine()) != null) {
                content.add(line);
            }
        } finally {
            input.close();
        }

        return content.toArray(new String[] {});
    }

    protected String toPath(Package packageName) {
        return toPath(packageName.getName());
    }

    protected String toPath(String packageName) {
        return packageName.replace(".", "/");
    }

    protected File getResourceFile(Class<?> clazz) {
        return new File(getClass().getResource("/" + toPath(clazz.getPackage()) + "/" + clazz.getSimpleName() + OUTPUT_FILE_SUFFIX).getFile());
    }

    protected File getOutputFile(String file) {
        return new File(OUTPUT_DIRECTORY, file);
    }

}
