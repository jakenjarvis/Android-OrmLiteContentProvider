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

import com.tojc.ormlite.android.compiler.sample.Pojo;
import com.tojc.ormlite.android.compiler.sample.Pojo2;
import com.tojc.ormlite.android.compiler.sample.Pojo3;
import com.tojc.ormlite.android.compiler.sample.Pojo4;
import com.tojc.ormlite.android.compiler.sample.PojoWithFields1;
import com.tojc.ormlite.android.compiler.sample.SuperPojo1;
import com.tojc.ormlite.android.compiler.sample.SuperPojo2;
import com.tojc.ormlite.android.compiler.sample.SuperPojo3;
import com.tojc.ormlite.android.compiler.sample.SuperPojo4;
import com.tojc.ormlite.android.compiler.sample.SuperPojo5;
import com.tojc.ormlite.android.compiler.sample.SuperPojo6;
import org.junit.Test;

import javax.annotation.processing.Processor;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

public class ContractAnnotationProcessorTest extends AbstractAnnotationProcessorTest {

    @Override
    protected Collection<Processor> getProcessors() {
        return Arrays.<Processor>asList(new ContractAnnotationProcessor());
    }

    // @Test
    // public void leafAnnotationOnNonCompositeMember() {
    // assertCompilationReturned(Kind.ERROR, 22, compileTestCase(Pojo.class));
    // }

    @Test
    public void validVerySimpleContractAnnotation() throws IOException {
        assertCompilationSuccessful(compileTestCase(Pojo.class));
        final String string = "target/generated-test/com/tojc/ormlite/android/compiler/sample/PojoContract.java";
        final String string2 = "target/test-classes/com/tojc/ormlite/android/compiler/sample/PojoContract.javasource";
        assertOutput(new File(string), new File(string2));
    }

    @Test
    public void validLessSimpleContractAnnotation() throws IOException {
        assertCompilationSuccessful(compileTestCase(Pojo2.class));
        final String string = "target/generated-test/com/tojc/ormlite/android/compiler/sample/LessSimplePojoContract2.java";
        final String string2 = "target/test-classes/com/tojc/ormlite/android/compiler/sample/LessSimplePojoContract2.javasource";
        assertOutput(new File(string), new File(string2));
    }

    @Test
    public void validDefaultContentUriAndContractAnnotation() throws IOException {
        assertCompilationSuccessful(compileTestCase(Pojo3.class));
        final String string = "target/generated-test/com/tojc/ormlite/android/compiler/sample/Pojo3Contract.java";
        final String string2 = "target/test-classes/com/tojc/ormlite/android/compiler/sample/Pojo3Contract.javasource";
        assertOutput(new File(string), new File(string2));
    }

    @Test
    public void validDefaultContentUriAndDefaultContentMimeTypeVndContractAnnotation() throws IOException {
        assertCompilationSuccessful(compileTestCase(Pojo4.class));
        final String string = "target/generated-test/com/tojc/ormlite/android/compiler/sample/Pojo4Contract.java";
        final String string2 = "target/test-classes/com/tojc/ormlite/android/compiler/sample/Pojo4Contract.javasource";
        assertOutput(new File(string), new File(string2));
    }

    @Test
    public void validFieldsContractAnnotation() throws IOException {
        assertCompilationSuccessful(compileTestCase(PojoWithFields1.class));
        final String string = "target/generated-test/com/tojc/ormlite/android/compiler/sample/PojoWithFields1Contract.java";
        final String string2 = "target/test-classes/com/tojc/ormlite/android/compiler/sample/PojoWithFields1Contract.javasource";
        assertOutput(new File(string), new File(string2));
    }

    @Test
    public void validSuperPojo() throws IOException {
        assertCompilationSuccessful(compileTestCase(SuperPojo1.class, SuperPojo2.class));
        final String string = "target/generated-test/com/tojc/ormlite/android/compiler/sample/SuperPojoContract.java";
        final String string2 = "target/test-classes/com/tojc/ormlite/android/compiler/sample/SuperPojoContract.javasource";
        assertOutput(new File(string), new File(string2));
    }

    @Test
    public void validSuperPojoWithDefaultContentUriAndDefaultContentMimeTypeVndContractAnnotation() throws IOException {
        assertCompilationSuccessful(compileTestCase(SuperPojo3.class, SuperPojo4.class));
        final String string = "target/generated-test/com/tojc/ormlite/android/compiler/sample/SuperPojoContract3.java";
        final String string2 = "target/test-classes/com/tojc/ormlite/android/compiler/sample/SuperPojoContract3.javasource";
        assertOutput(new File(string), new File(string2));
    }

    @Test
    public void validSuperPojoMixedWithDefaultContentUriAndDefaultContentMimeTypeVndContractAnnotation() throws IOException {
        assertCompilationSuccessful(compileTestCase(SuperPojo5.class, SuperPojo6.class));
        final String string = "target/generated-test/com/tojc/ormlite/android/compiler/sample/SuperPojoContract5.java";
        final String string2 = "target/test-classes/com/tojc/ormlite/android/compiler/sample/SuperPojoContract5.javasource";
        assertOutput(new File(string), new File(string2));
    }
}
