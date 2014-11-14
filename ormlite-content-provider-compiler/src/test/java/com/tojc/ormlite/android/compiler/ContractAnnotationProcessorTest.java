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

import com.tojc.ormlite.android.compiler.sample.CombinedPojo1;
import com.tojc.ormlite.android.compiler.sample.CombinedPojo2;
import com.tojc.ormlite.android.compiler.sample.CombinedPojo3;
import com.tojc.ormlite.android.compiler.sample.CombinedPojo4;
import com.tojc.ormlite.android.compiler.sample.CombinedPojo5;
import com.tojc.ormlite.android.compiler.sample.CombinedPojo6;
import com.tojc.ormlite.android.compiler.sample.Pojo;
import com.tojc.ormlite.android.compiler.sample.Pojo2;
import com.tojc.ormlite.android.compiler.sample.Pojo3;
import com.tojc.ormlite.android.compiler.sample.Pojo4;
import com.tojc.ormlite.android.compiler.sample.PojoWithFields1;
import com.tojc.ormlite.android.compiler.sample.PojoWithFields2;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;

import javax.annotation.processing.Processor;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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
    public void a0100ValidVerySimpleContractAnnotation() throws IOException {
        assertCompilationSuccessful(compileTestCase(Pojo.class));
        final String string = "build/source/generated-test/com/tojc/ormlite/android/compiler/sample/PojoContract.java";
        final String string2 = "build/resources/test/com/tojc/ormlite/android/compiler/sample/PojoContract.javasource";
        assertOutput(new File(string), new File(string2));
    }

    @Test
    public void a0101ValidLessSimpleContractAnnotation() throws IOException {
        assertCompilationSuccessful(compileTestCase(Pojo2.class));
        final String string = "build/source/generated-test/com/tojc/ormlite/android/compiler/sample/LessSimplePojoContract2.java";
        final String string2 = "build/resources/test/com/tojc/ormlite/android/compiler/sample/LessSimplePojoContract2.javasource";
        assertOutput(new File(string), new File(string2));
    }

    @Test
    public void a0200ValidDefaultContentUriAndContractAnnotation() throws IOException {
        assertCompilationSuccessful(compileTestCase(Pojo3.class));
        final String string = "build/source/generated-test/com/tojc/ormlite/android/compiler/sample/Pojo3Contract.java";
        final String string2 = "build/resources/test/com/tojc/ormlite/android/compiler/sample/Pojo3Contract.javasource";
        assertOutput(new File(string), new File(string2));
    }

    @Test
    public void a0201ValidDefaultContentUriAndDefaultContentMimeTypeVndContractAnnotation() throws IOException {
        assertCompilationSuccessful(compileTestCase(Pojo4.class));
        final String string = "build/source/generated-test/com/tojc/ormlite/android/compiler/sample/Pojo4Contract.java";
        final String string2 = "build/resources/test/com/tojc/ormlite/android/compiler/sample/Pojo4Contract.javasource";
        assertOutput(new File(string), new File(string2));
    }

    @Test
    public void a0300ValidFieldsContractAnnotation() throws IOException {
        assertCompilationSuccessful(compileTestCase(PojoWithFields1.class));
        final String string = "build/source/generated-test/com/tojc/ormlite/android/compiler/sample/PojoWithFields1Contract.java";
        final String string2 = "build/resources/test/com/tojc/ormlite/android/compiler/sample/PojoWithFields1Contract.javasource";
        assertOutput(new File(string), new File(string2));
    }

    @Test
    public void a0301ValidFieldsWithColumnNameContractAnnotation() throws IOException {
        assertCompilationSuccessful(compileTestCase(PojoWithFields2.class));
        final String string = "build/source/generated-test/com/tojc/ormlite/android/compiler/sample/PojoWithFields2Contract.java";
        final String string2 = "build/resources/test/com/tojc/ormlite/android/compiler/sample/PojoWithFields2Contract.javasource";
        assertOutput(new File(string), new File(string2));
    }

    @Test
    public void a0400ValidSuperPojo() throws IOException {
        assertCompilationSuccessful(compileTestCase(CombinedPojo1.class, CombinedPojo2.class));
        final String string = "build/source/generated-test/com/tojc/ormlite/android/compiler/sample/CombinedPojoContract1.java";
        final String string2 = "build/resources/test/com/tojc/ormlite/android/compiler/sample/CombinedPojoContract1.javasource";
        assertOutput(new File(string), new File(string2));
    }

    @Test
    public void a0401ValidSuperPojoWithDefaultContentUriAndDefaultContentMimeTypeVndContractAnnotation() throws IOException {
        assertCompilationSuccessful(compileTestCase(CombinedPojo3.class, CombinedPojo4.class));
        final String string = "build/source/generated-test/com/tojc/ormlite/android/compiler/sample/CombinedPojoContract2.java";
        final String string2 = "build/resources/test/com/tojc/ormlite/android/compiler/sample/CombinedPojoContract2.javasource";
        assertOutput(new File(string), new File(string2));
    }

    @Test
    public void a0402ValidSuperPojoMixedWithDefaultContentUriAndDefaultContentMimeTypeVndContractAnnotation() throws IOException {
        assertCompilationSuccessful(compileTestCase(CombinedPojo5.class, CombinedPojo6.class));
        final String string = "build/source/generated-test/com/tojc/ormlite/android/compiler/sample/CombinedPojoContract3.java";
        final String string2 = "build/resources/test/com/tojc/ormlite/android/compiler/sample/CombinedPojoContract3.javasource";
        assertOutput(new File(string), new File(string2));
    }
}
