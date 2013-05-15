package com.tojc.ormlite.android.compiler;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.processing.Processor;
import javax.tools.Diagnostic.Kind;

import org.junit.Test;

import com.tojc.ormlite.android.compiler.sample.Pojo;

public class ContractAnnotationProcessorTest extends AbstractAnnotationProcessorTest {

    @Override
    protected Collection<Processor> getProcessors() {
        return Arrays.<Processor>asList(new ContractAnnotationProcessor());
    }

    @Test
    public void leafAnnotationOnNonCompositeMember() {
        assertCompilationReturned(Kind.ERROR, 22, compileTestCase(Pojo.class));
    }

    @Test
    public void validCompositeAnnotation() {
        assertCompilationSuccessful(compileTestCase(Pojo.class));
    }

}
