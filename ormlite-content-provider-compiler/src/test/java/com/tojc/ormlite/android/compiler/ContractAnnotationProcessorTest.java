package com.tojc.ormlite.android.compiler;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import javax.annotation.processing.Processor;

import org.junit.Test;

import com.tojc.ormlite.android.compiler.sample.Pojo;

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
    public void validContractAnnotation() {
        assertCompilationSuccessful(compileTestCase(Pojo.class));
        String string = "target/generated-test/com/tojc/ormlite/android/compiler/sample/PojoContract.java";
        String string2 = "target/test-classes/com/tojc/ormlite/android/compiler/sample/PojoContract.java";
        assertOutput(new File(string), new File(string2));
    }
}
