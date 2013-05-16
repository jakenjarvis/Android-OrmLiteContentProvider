package com.tojc.ormlite.android.compiler;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import javax.annotation.processing.Processor;

import org.junit.Test;

import com.tojc.ormlite.android.compiler.sample.Pojo;
import com.tojc.ormlite.android.compiler.sample.Pojo2;
import com.tojc.ormlite.android.compiler.sample.Pojo3;

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
    public void validVerySimpleContractAnnotation() {
        assertCompilationSuccessful(compileTestCase(Pojo.class));
        String string = "target/generated-test/com/tojc/ormlite/android/compiler/sample/PojoContract.java";
        String string2 = "target/test-classes/com/tojc/ormlite/android/compiler/sample/PojoContract.javasource";
        assertOutput(new File(string), new File(string2));
    }

    @Test
    public void validLessSimpleContractAnnotation() {
        assertCompilationSuccessful(compileTestCase(Pojo2.class));
        String string = "target/generated-test/com/tojc/ormlite/android/compiler/sample/LessSimplePojoContract2.java";
        String string2 = "target/test-classes/com/tojc/ormlite/android/compiler/sample/LessSimplePojoContract2.javasource";
        assertOutput(new File(string), new File(string2));
    }

    @Test
    public void validDefaultContentUriAndContractAnnotation() {
        assertCompilationSuccessful(compileTestCase(Pojo3.class));
        String string = "target/generated-test/com/tojc/ormlite/android/compiler/sample/Pojo3Contract.java";
        String string2 = "target/test-classes/com/tojc/ormlite/android/compiler/sample/Pojo3Contract.javasource";
        assertOutput(new File(string), new File(string2));
    }

}
